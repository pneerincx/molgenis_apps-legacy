/**
 * @author Robert Wagner
 * @date Feb 2011
 * 
 * This class provides a secured login to the molgenis suite by implementing row and table level
 * security. 
 */
package org.molgenis.auth;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.security.Login;
import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.util.Entity;

import org.molgenis.auth.MolgenisGroup;
import org.molgenis.auth.MolgenisPermission;
import org.molgenis.auth.MolgenisRole;
import org.molgenis.auth.MolgenisUser;
import org.molgenis.auth.service.MolgenisUserService;
import org.molgenis.auth.util.PasswordHasher;

public class DatabaseLogin implements Login, Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Specifies an enumeration for valid Permissions
	 * @author Jessica
	 */
	enum Permission {
		read, write, own
	};

	/** The current user that has been authenticated (if any) */
	MolgenisUser user;
	/** The groups this user is member of */
	List<MolgenisGroup> groups = new ArrayList<MolgenisGroup>();

	/** Map to quickly retrieve a permission */
	Map<String, Permission> readMap    = new TreeMap<String, Permission>();
	Map<String, Permission> writeMap   = new TreeMap<String, Permission>();
	Map<String, Permission> executeMap = new TreeMap<String, Permission>();
	Map<String, Permission> ownMap     = new TreeMap<String, Permission>();

	Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	public DatabaseLogin() {
		logger.debug("DatabaseLogin()");
	}
	
	/** Constructor used to login anonymous
	 * 
	 * @param db database to log in to.
	 */
	public DatabaseLogin(Database db)
	{
		this.login(db, "anonymous", "anonymous");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getUserId() {
		if (user != null)
			return user.getId();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserName() {
		if (user != null)
			return user.getName();
		return null;
	}
	
	public String getFullUserName() {
		return user.getFirstname() + " " + user.getLastname();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Note: Anonymous is automatically logged in but does not count as being authenticated
	 */
	@Override
	public boolean isAuthenticated() {
//		return user != null;
		if (user == null)
			return false;
		else if ("anonymous".equals(user.getName()))
			return false;
		else
			return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean login(Database db, String name, String password)
	{
		// username is required
		if (name == null || "".equals(name))
			return false;

		// password is required
		if (password == null || "".equals(password))
			return false;

		try
		{
			PasswordHasher md5       = new PasswordHasher();

			password                 = md5.toMD5(password); // encode password

			List<MolgenisUser> users = db.query(MolgenisUser.class).eq(MolgenisUser.NAME, name).eq(MolgenisUser.PASSWORD, password).eq(MolgenisUser.ACTIVE, true).find();

			if (users.size() == 1 && name.equals(users.get(0).getName()) && password.equals(users.get(0).getPassword()))
			{
				user = users.get(0);
				this.reload(db);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//TODO: What to do here?
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout()
	{
		this.user = null;
	}

	/** Reloads all permission settings for this user from database.
	 * 
	 * If user is null, anonymous is logged in.
	 *  Note: calling reload refreshes the permissions cache map. 
	 * 
	 */
	@Override
	public void reload(Database db) throws DatabaseException, ParseException {
		if (this.user == null)
			this.login(db, "anonymous", "anonymous");
//			return;

		// // get the groups this user is member of
		// if (currentPatient != null)
		// groups = db.query(MolgenisUserGroup.class).equals("members",
		// currentPaFtient.getId()).find();
		// else {
		MolgenisUserService service = MolgenisUserService.getInstance(db);
		List<Integer> groupdIds     = service.findGroupIds(this.user);
		
		if (!groupdIds.isEmpty()) {
			this.groups = db.query(MolgenisGroup.class).in(MolgenisGroup.ID, groupdIds).find();
		}
		// }

		// clear the permission maps
		this.readMap.clear();
		this.writeMap.clear();
		this.executeMap.clear();
		this.ownMap.clear();
		
		//get permissions for this user
		loadPermissions(db, user);

		for (MolgenisGroup group : groups) {
			loadPermissions(db, group);
		}

	}
	
	/** Reload the permissions map which contains all permissions, for the logged in user.  
	 * 
	 * @param db database to load permissions from
	 * @param role the role to load permissions for
	 * @throws DatabaseException
	 * @throws ParseException
	 */
	private void loadPermissions(Database db, MolgenisRole role) throws DatabaseException, ParseException
	{
		MolgenisUserService service          = MolgenisUserService.getInstance(db);
		
		List<Integer> roleIdList             = service.findGroupIds(role);
		List<MolgenisPermission> permissions = db.query(MolgenisPermission.class).in(MolgenisPermission.ROLE_, roleIdList).find();
		
		for (MolgenisPermission permission : permissions)
		{
			if ("read".equals(permission.getPermission()))
				this.readMap.put(permission.getEntity_ClassName(), Permission.read);
			else if ("write".equals(permission.getPermission()))
			{
				this.readMap.put(permission.getEntity_ClassName(), Permission.read);
				this.writeMap.put(permission.getEntity_ClassName(), Permission.write);
			}
			else if ("own".equals(permission.getPermission()))
			{
				this.readMap.put(permission.getEntity_ClassName(), Permission.read);
				this.writeMap.put(permission.getEntity_ClassName(), Permission.write);
				this.ownMap.put(permission.getEntity_ClassName(), Permission.own);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("Login(user=" + this.getUserName() + " roles=");
		 for (int i = 0; i < groups.size(); i++)
		 {
			 if (i > 0)
				 result.append("," + groups.get(i).getName());
			 else
				 result.append(groups.get(i).getName());
		 }
		 for (String key : this.readMap.keySet())
			 result.append(" " + key + "=" + this.readMap.get(key));
		 for (String key : this.writeMap.keySet())
			 result.append(" " + key + "=" + this.writeMap.get(key));
		 result.append(")");

		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLoginRequired() {
		return false;
	}

	/**
	 * Deprecated.
	 */
	@Override
	public QueryRule getRowlevelSecurityFilters(Class<? extends Entity> klazz)
	{
//		this.user.getAllowedToView();
		
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canRead(Class<? extends Entity> entityClass)	throws DatabaseException
	{
		if (this.isAuthenticated() && this.user.getSuperuser())
			return true;

		String className = entityClass.getName();

		if (className.startsWith("org.molgenis.auth.Molgenis"))
			return true;

		if (this.readMap.containsKey(className))
			return true;

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canWrite(Class<? extends Entity> entityClass) throws DatabaseException
	{
		if (this.isAuthenticated() && this.user.getSuperuser())
			return true;

		String className = entityClass.getName();

		if (this.writeMap.containsKey(className))
			return true;
		
		return false;
	}

	public boolean owns(Class<? extends Entity> entityClass) throws DatabaseException
	{
		if (this.isAuthenticated() && this.user.getSuperuser())
			return true;

		String className = entityClass.getName();

		if (className.startsWith("org.molgenis.auth.Molgenis"))
			return true;

		if (this.ownMap.containsKey(className))
			return true;

		return false;
	}

	/**
	 * Indicates whether the user has permissions to read data from this entity. Note:
	 * if row-level security is activated, only rows for which the user has been given
	 * permission will display. 
	 * 
	 * @param Entity the entity to get permission from
	 * @return boolean true if can read, false otherwise
	 * @throws DatabaseException 
	 * @throws DatabaseException 
	 */
	@Override
	public boolean canRead(Entity entity) throws DatabaseException
	{
		if (this.isAuthenticated() && this.user.getSuperuser())
			return true;

		if (this.isImplementing(entity, "org.molgenis.auth.Authorizable"))
		{
			if (!this.isAuthenticated())
				return false;

			Integer id = (Integer) entity.get("canRead");
			
			if (this.getUserId().equals(id)) 
				return true;
			
			// Write permission implicits read permission
			return this.canWrite(entity);
		}

		// Not implementing Authorizable -> apply table level security
		return this.canRead(entity.getClass());
	}

	/**
	 * Indicates whether the user has permissions to write (and read) data from this entity. Note:
	 * if row-level security is activated, only rows for which the user has been given
	 * permission will display as editable. 
	 * 
	 * @param Entity the entity to get permission from
	 * @return boolean true if can write, false otherwise
	 * @throws DatabaseException 
	 * @throws DatabaseException 
	 */
	@Override
	public boolean canWrite(Entity entity) throws DatabaseException
	{
		if (this.isAuthenticated() && this.user.getSuperuser())
			return true;

		if (this.isImplementing(entity, "org.molgenis.auth.Authorizable"))
		{
			if (!this.isAuthenticated())
			{
				entity.setReadonly(true);
				return false;
			}

			Integer id = (Integer) entity.get("canWrite");
			
			if (this.getUserId().equals(id)) 
				return true;

			// Ownership implies write permission
			return this.owns(entity);
		}

		// Not implementing Authorizable -> apply table level security
		return this.canWrite(entity.getClass());
	}

	public boolean owns(Entity entity) throws DatabaseException
	{
		if (this.isAuthenticated() && this.user.getSuperuser())
			return true;

		if (this.isImplementing(entity, "org.molgenis.auth.Authorizable"))
		{
			if (!this.isAuthenticated())
				return false;

			Integer id = (Integer) entity.get("owns");
			
			if (id.equals(this.getUserId()))
				return true;
			
			return false;
		}

		// Not implementing Authorizable -> apply table level security
		return this.owns(entity.getClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canRead(ScreenModel<?> screen)
	{
		if (this.isAuthenticated() && this.user.getSuperuser())
			return true;

		String className = screen.getClass().getName();

		//if (className.equals("app.ui.UserLoginPlugin"))
		//	return true;

		if (this.readMap.containsKey(className))
			return true;

		return false;
	}

	/** Helper method to check if an entity is implementing the authorizable interface,
	 * i.e. to check if we should implement row-level security.
	 * 
	 * @param entity
	 * @param interfaceName
	 * @return
	 */
	private boolean isImplementing(Entity entity, String interfaceName)
	{
		if (StringUtils.isEmpty(interfaceName))
			return false;

		Class<?>[] interfaces = entity.getClass().getInterfaces();
		
		for (Class<?> interface_ : interfaces)
		{
			if (interface_.getName().equals(interfaceName))
				return true;
		}
		
		return false;
	}
}
