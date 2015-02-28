/**
 * 
 */
package com.maomao.app.citybuy.activity.accounts.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.query.Query;
import com.maomao.app.citybuy.activity.accounts.Citytogo;
import com.maomao.app.citybuy.activity.accounts.DiaryInfo;
import com.maomao.app.citybuy.activity.accounts.DiaryInfoCaogao;
import com.maomao.app.citybuy.activity.accounts.GalleryImage;
import com.maomao.app.citybuy.activity.accounts.JingyanItem;
import com.maomao.app.citybuy.activity.accounts.UserData;

/**
 * @author German Viscuso
 * 
 */
public class DBHelper {
	private static ObjectContainer oc = null;
	private static Context context;
	private static DBHelper instance = null;

	private static final String DATABASE_NAME = "citytogo.yap";

	public synchronized static DBHelper getInstance() {
		if (instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}

	public void initDb() {
		db();
	}

	public static void initContext(Context ctx) {
		context = ctx;
	}

	private DBHelper(Context ctx) {
		context = ctx;
	}

	private ObjectContainer db() {
		try {
			if (oc == null || oc.ext().isClosed())
				oc = Db4o.openFile(dbConfig(), db4oDBFullPath(context));
			return oc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Configuration dbConfig() {
		// 创建一个新的配置实例
		Configuration config = Db4o.newConfiguration();
		// 为配置指定特定的反射
		// config.reflectWith(new
		// JdkReflector(this.getClass().getClassLoader()));
		// Configuration config = Db4o.newConfiguration();
		config.objectClass(Citytogo.class).objectField("id").indexed(true);
		config.objectClass(JingyanItem.class).objectField("itemid")
				.indexed(true);
		return config;
	}

	public String db4oDBFullPath(Context ctx) {
		// return SdcardManager.getCameraFilePath()+"/" + DATABASE_NAME;
		return ctx.getDir("data", 0) + "/" + DATABASE_NAME;
	}

	/**
	 * Close database connection
	 */
	public void close() {
		if (oc != null) {
			oc.close();
			oc = null;
		}
	}

	public void commit() {
		db().commit();
	}

	public void rollback() {
		db().rollback();
	}

	public void deleteDatabase() {
		close();
		new File(db4oDBFullPath(context)).delete();
	}

	public void backup(String path) {
		db().ext().backup(path);
	}

	public void restore(String path) {
		deleteDatabase();
		new File(path).renameTo(new File(db4oDBFullPath(context)));
		new File(path).delete();
	}

	public void upadateUserData(String avatarurl, String nickname,
			String address) {
		UserData userData = getUserData();
		// userData.setAttHouseCount(attentionCount);
		// userData.setHouseCount(houseCount);
		if (!TextUtils.isEmpty(avatarurl) || !avatarurl.equals("null")) {
			userData.setAvatarurl(avatarurl);
		}
		userData.setAddress(address);
		userData.setNickname(nickname);
		saveUserData(userData);
	}

	public void deleteUserData() {
		UserData userData = getUserData();
		if (userData != null) {
			db().delete(userData);
			db().commit();
		}
	}

	public void saveUserData(UserData userData) {
		db().store(userData);
		db().commit();
	}

	public UserData getUserData() {
		Query query = db().query();
		query.constrain(UserData.class);
		ObjectSet<UserData> objectSet = query.execute();
		while (objectSet.hasNext()) {
			return objectSet.next();
		}
		return null;
	}

	public void saveCitytogo(Citytogo citytogo) {
		Query query = db().query();
		query.constrain(Citytogo.class);
		query.descend("id").constrain(citytogo.getId());
		ObjectSet<Citytogo> objectSet = query.execute();
		if (!objectSet.hasNext()) {
			db().store(citytogo);
			db().commit();
		}
	}

	public void deleteCitytogo(Citytogo citytogo) {
		Citytogo tempCitytogo = null;
		Query query = db().query();
		query.constrain(Citytogo.class);
		query.descend("id").constrain(citytogo.getId());
		ObjectSet<Citytogo> objectSet = query.execute();
		while (objectSet.hasNext()) {
			tempCitytogo = objectSet.next();
		}
		if (tempCitytogo != null) {
			db().delete(tempCitytogo);
			db().commit();
		}
	}

	public boolean isExitByCitytogo(Citytogo citytogo) {
		Query query = db().query();
		query.constrain(Citytogo.class);
		query.descend("id").constrain(citytogo.getId());
		ObjectSet<Citytogo> objectSet = query.execute();
		return objectSet.hasNext();
	}

	public List<Citytogo> findAllCitytogo() {
		List<Citytogo> datas = new ArrayList<Citytogo>();
		Query query = db().query();
		query.constrain(Citytogo.class);
		ObjectSet<Citytogo> objectSet = query.execute();
		while (objectSet.hasNext()) {
			datas.add(objectSet.next());
		}
		return datas;
	}

	public void saveJingyanItem(JingyanItem jingyanItem) {
		Query query = db().query();
		query.constrain(JingyanItem.class);
		query.descend("itemid").constrain(jingyanItem.getItemid());
		ObjectSet<JingyanItem> objectSet = query.execute();
		if (!objectSet.hasNext()) {
			db().store(jingyanItem);
			db().commit();
		}
	}

	public void deleteJingyanItem(JingyanItem jingyanItem) {
		JingyanItem tempJingyanItem = null;
		Query query = db().query();
		query.constrain(JingyanItem.class);
		query.descend("itemid").constrain(jingyanItem.getItemid());
		ObjectSet<JingyanItem> objectSet = query.execute();
		while (objectSet.hasNext()) {
			tempJingyanItem = objectSet.next();
		}
		if (tempJingyanItem != null) {
			db().delete(tempJingyanItem);
			db().commit();
		}
	}

	public boolean isExitByJingyanItem(JingyanItem jingyanItem) {
		Query query = db().query();
		query.constrain(JingyanItem.class);
		query.descend("itemid").constrain(jingyanItem.getItemid());
		ObjectSet<JingyanItem> objectSet = query.execute();
		return objectSet.hasNext();
	}

	public List<JingyanItem> findAllJingyanItem() {
		List<JingyanItem> datas = new ArrayList<JingyanItem>();
		Query query = db().query();
		query.constrain(JingyanItem.class);
		ObjectSet<JingyanItem> objectSet = query.execute();
		while (objectSet.hasNext()) {
			datas.add(objectSet.next());
		}
		return datas;
	}

	public void saveGalleryImage(GalleryImage galleryImage) {
		Query query = db().query();
		query.constrain(GalleryImage.class);
		query.descend("id").constrain(galleryImage.getId());
		ObjectSet<GalleryImage> objectSet = query.execute();
		if (!objectSet.hasNext()) {
			db().store(galleryImage);
			db().commit();
		}
	}

	public void deleteGalleryImage(GalleryImage galleryImage) {
		GalleryImage tempGalleryImage = null;
		Query query = db().query();
		query.constrain(GalleryImage.class);
		query.descend("id").constrain(galleryImage.getId());
		ObjectSet<GalleryImage> objectSet = query.execute();
		while (objectSet.hasNext()) {
			tempGalleryImage = objectSet.next();
		}
		if (tempGalleryImage != null) {
			db().delete(tempGalleryImage);
			db().commit();
		}
	}

	public boolean isExitByGalleryImage(GalleryImage galleryImage) {
		Query query = db().query();
		query.constrain(GalleryImage.class);
		query.descend("id").constrain(galleryImage.getId());
		ObjectSet<GalleryImage> objectSet = query.execute();
		return objectSet.hasNext();
	}

	public List<GalleryImage> findAllGalleryImage() {
		List<GalleryImage> datas = new ArrayList<GalleryImage>();
		Query query = db().query();
		query.constrain(GalleryImage.class);
		ObjectSet<GalleryImage> objectSet = query.execute();
		while (objectSet.hasNext()) {
			datas.add(objectSet.next());
		}
		return datas;
	}

	public void saveDiaryInfo(DiaryInfo diaryInfo) {
		Query query = db().query();
		query.constrain(DiaryInfo.class);
		query.descend("diaryid").constrain(diaryInfo.getDiaryid());
		ObjectSet<DiaryInfo> objectSet = query.execute();
		if (!objectSet.hasNext()) {
			db().store(diaryInfo);
			db().commit();
		}
	}

	public void deleteDiaryInfo(DiaryInfo diaryInfo) {
		DiaryInfo tempdiaryInfo = null;
		Query query = db().query();
		query.constrain(DiaryInfo.class);
		query.descend("diaryid").constrain(diaryInfo.getDiaryid());
		ObjectSet<DiaryInfo> objectSet = query.execute();
		while (objectSet.hasNext()) {
			tempdiaryInfo = objectSet.next();
		}
		if (tempdiaryInfo != null) {
			db().delete(tempdiaryInfo);
			db().commit();
		}
	}

	public boolean isExitDiaryInfo(String diaryid) {
		Query query = db().query();
		query.constrain(DiaryInfo.class);
		query.descend("diaryid").constrain(diaryid);
		ObjectSet<DiaryInfo> objectSet = query.execute();
		return objectSet.hasNext();
	}

	public List<DiaryInfo> findAllDiaryInfo() {
		List<DiaryInfo> datas = new ArrayList<DiaryInfo>();
		Query query = db().query();
		query.constrain(DiaryInfo.class);
		ObjectSet<DiaryInfo> objectSet = query.execute();
		while (objectSet.hasNext()) {
			datas.add(objectSet.next());
		}
		return datas;
	}

	public void saveDiaryCaogao(DiaryInfoCaogao diary) {
		db().store(diary);
		db().commit();
	}

	public void deleteDiaryCaogao(DiaryInfoCaogao diary) {
		db().delete(diary);
		db().commit();
	}

	public List<DiaryInfoCaogao> findAllDiaryCaogao() {
		List<DiaryInfoCaogao> datas = new ArrayList<DiaryInfoCaogao>();
		Query query = db().query();
		query.constrain(DiaryInfoCaogao.class);
		ObjectSet<DiaryInfoCaogao> objectSet = query.execute();
		while (objectSet.hasNext()) {
			datas.add(objectSet.next());
		}
		return datas;
	}

}
