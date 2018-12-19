package com.github.zhangyang.camera_picker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import com.github.zhangyang.camera_picker.exception.ContentException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtils {

	private static FileUtils instance;

	//需要修改
	private static final String TAG = "YanZi";
	private FileUtils() {

	}

	public static FileUtils getInstance() {
		if (instance == null) {
			synchronized (FileUtils.class) {
				if (instance == null) {
					instance = new FileUtils();
				}
			}
		}
		return instance;
	}

	private final String ROOT_DIR = "AroundPlay";

	/**
	 * 缓存文件
	 **/
	public String getCacheDir(Context context) {
		return getDir(context,"cache");
	}

	/**
	 * 缓存图片
	 **/
	public String getIconDir(Context context) {
		return getDir(context,"icon");
	}

	private String getDir(Context context,String string) {
		if (isSDAvailable()) {
			return getSDDir(string);
		} else {
			return getDataDir(context,string);
		}
	}

	/**
	 * 描述：获取到手机内存的目录
	 */
	private String getDataDir(Context context,String string) {
		// data/data/包名/cache/cache
		return context.getCacheDir().getAbsolutePath() + File.separator + string;
	}

	/**
	 * 描述：获取SD卡的目录
	 */
	private String getSDDir(String string) {
		StringBuffer sb = new StringBuffer();
		String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();// /mnt/sdcard
		sb.append(absolutePath);
		sb.append(File.separator);//  /mnt/sdcard/
		sb.append(ROOT_DIR);// /mnt/sdcard/AroundPlay
		sb.append(File.separator);// /mnt/sdcard/AroundPlay/
		sb.append(string);// /mnt/sdcard/AroundPlay/cache
		//sb.append(File.separator);// /mnt/sdcard/AroundPlay/cache/
		String filePath = sb.toString();
		File file = new File(filePath);

		if (!file.exists() || !file.isDirectory()) {
			if (file.mkdirs()) {
				return file.getAbsolutePath();
			} else {
				return "";
			}
		}

		return file.getAbsolutePath();
	}

	/**
	 * 描述：判断SD卡是否可用
	 */
	private boolean isSDAvailable() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 关闭IO流
	 *
	 * @param out
	 */
	public void close(Closeable out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从文本文件中读取文本数据，中文乱码问题，编码格式用utf8
	 *
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String getText(String filePath) throws Exception {

//		File file = new File(filePath);
		BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "gb2312"));
		String temp = "";
		StringBuilder sb = new StringBuilder();
		while ((temp = bufr.readLine()) != null) {
			sb.append(temp);
		}
		bufr.close();
		return sb.toString();
	}


	/**
	 * 把头像图片存储到SD卡固定目录下
	 */
	public void InputSDCordImg(Context context,ByteArrayInputStream inputStream) {
		try {
			String picPath = SDCardUtil.getInstance(context).getIconPath();
			File file = new File(picPath, Constants.PIC_NAME);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int count = -1;
			byte[] buffer = new byte[1024];
			while ((count = inputStream.read(buffer)) != -1) {
				System.out.println("FileUtils Uploadfile count = " + count);
				fileOutputStream.write(buffer, 0, count);
				fileOutputStream.flush();
			}
			inputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/****
	 * 从固定路径拿图片
	 **/
	public String getPicNamePath(Context context) {
		String picPath = SDCardUtil.getInstance(context).getIconPath() + File.separator + Constants.PIC_NAME;
		File file = new File(picPath);
		if (file.exists()) {
			return picPath;
		} else {
			return null;
		}
	}

	/****
	 * 从固定路径中删除图片
	 **/
	public void deleteImg(Context context,String fileName) {
		String picPath = SDCardUtil.getInstance(context).getIconPath() + File.separator + fileName;
		File file = new File(picPath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 判断固定目录下是否存在此文件
	 **/
	public boolean isExistsFile(Context context,String filePath) {
		String picPath = SDCardUtil.getInstance(context).getIconPath() + File.separator + filePath;
		File file = new File(picPath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 把头像图片存储到SD卡固定目录下
	 */
	public void InputSDCordImgTo(ByteArrayInputStream inputStream, String filePath) {
		try {
			File file = new File(filePath);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int count = -1;
			byte[] buffer = new byte[1024];
			while ((count = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, count);
				fileOutputStream.flush();
			}
			inputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***把原文件删除，并更名为原文件的名字***/
	public void updateFileNames(Context context) {
		String picPath = SDCardUtil.getInstance(context).getIconPath() + File.separator;
		File fileOld = new File(picPath + Constants.PIC_NAME);
		File fileNew = new File(picPath + Constants.PIC_NAME_TYPE);
		//判断文件是否存在
		if (fileOld.exists()) {
			String pic_name = fileOld.getName();
			deleteImg(context,Constants.PIC_NAME);
			fileNew.renameTo(new File(picPath  + pic_name));
		}else{
			UiUtils.getInstance().showToast(context,"文件不存在！");
		}
	}

	/**
	 * 进行删除文件
	 * @param filePath
	 */
	public void deleteFile(String filePath){
		File file=new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}

	/***
	 *
	 * 照相部分.........
	 */
	/**初始化保存路径
	 * @return
	 */
	private static String initPath(Context context){
		String imageFilePath = SDCardUtil.getInstance(context).getUploadPicPath();
		return imageFilePath;
	}

	/**照相部分的保存Bitmap到sdcard
	 * @param b
	 */
	public static void saveBitmapPng(Context context,Bitmap b,List<String> filePathList,int qualtity){

		String path = initPath(context);
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".png";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.PNG, qualtity, bos);
			filePathList.add(0,jpegName);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
		}
	}

	public static String saveBitmapJPG(Context context,Bitmap b,List<String> filePathList,int qualtity)throws ContentException{
		String path = initPath(context);
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, qualtity, bos);
			filePathList.add(0, jpegName);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
			return jpegName;
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
			throw new ContentException("保存图片失败!");
		}
	}

	/**截图部分的保存Bitmap到sdcard
	 * @param b
	 */
	public static String saveBitmapPng(Context context,Bitmap b,int qualtity)throws ContentException{
		String path = initPath(context);
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".png";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.PNG, qualtity, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
			return jpegName;
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
			throw new ContentException("保存图片失败!");
		}
	}

	public static String saveBitmap(Context context,Bitmap b,long time,int type,List<String> pathList)throws ContentException{
		String path = initPath(context);
		String jpegName = path + "/" + time+"_"+type+".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			pathList.add(0, jpegName);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
			return jpegName;
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
			throw new ContentException("保存图片失败!");
		}
	}

	public static String saveBitmap(Context context,Bitmap b,long time,int type)throws ContentException{
		String path = initPath(context);
		String jpegName = path + "/" + time+"_"+type+".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
			return jpegName;
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
			throw new ContentException("保存图片失败!");
		}
	}

	public static String saveBitmapJPG(Context context,Bitmap b,long time,int type,int qualtity)throws ContentException{
		String path = initPath(context);
		String jpegName = path + "/" + time+"_"+type+".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, qualtity, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
			return jpegName;
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
			throw new ContentException("保存图片失败!");
		}
	}

	public static String saveBitmapBytes(Context context,byte bytes[],long time,int type)throws ContentException{
		String path = initPath(context);
		String jpegName = path + "/" + time+"_"+type+".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
			byte[] byteArray=new byte[1024];
			int length=-1;
			try {
				while ((length=byteArrayInputStream.read(byteArray,0,byteArray.length))!=-1) {
					fout.write(byteArray,0,length);
                }

				fout.close();
				byteArrayInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.i(TAG, "saveBitmap成功");
			return jpegName;
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
			throw new ContentException("保存图片失败!");
		}
	}

	/**截图部分的保存Bitmap到sdcard
	 * @param b
	 */
	public static String saveBitmapJPG(Context context,Bitmap b,int qualtity)throws ContentException{
		String path = initPath(context);
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, qualtity, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
			return jpegName;
		} catch (IOException e) {
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
			throw new ContentException("保存图片失败!");
		}
	}

	public static List<String> getFilePath(Context context,int size){
		File file=new File(initPath(context));
		String[] fileNameList=file.list();
		List<String> filePathList=new ArrayList<String>();
		String dir=initPath(context);
		if(fileNameList!=null && fileNameList.length>0){
			for(int i=0;i<(size>fileNameList.length?fileNameList.length:size);++i){
				filePathList.add(dir+ File.separator+fileNameList[i]);
			}
		}
		return  filePathList;
	}

	/**
	 * 进行将InputStream写入到对应的目录
	 * @param inputStream
	 * @param destDir
	 */
	public void writeToDir(InputStream inputStream, String destDir) {
		FileOutputStream fileOutputStream=null;
		BufferedInputStream fileInputStream=null;
		try {
			fileOutputStream=new FileOutputStream(destDir);
			fileInputStream=new BufferedInputStream(inputStream);
			int length=-1;
			byte buffers[]=new byte[1024];
			while((length=fileInputStream.read(buffers,0,buffers.length))!=-1){
				fileOutputStream.write(buffers,0,length);
			}
			fileOutputStream.close();
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fileOutputStream!=null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 保存错误信息到文件中
	 *
	 * @param
	 * @return  返回文件名称,便于将文件传送到服务器
	 */
	public void saveCrashInfo2File(Context context,Map<String,String> infos) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		try {
			//if (BaseApplication.hasSDcard()){
			String dir = SDCardUtil.getInstance(context).getDebugPath();
			File fileDirectroy=new File(dir);
			if(!fileDirectroy.exists()){
				fileDirectroy.mkdir();
			}
			File fire = new File(fileDirectroy,"log.txt");
			if(!fire.exists()) fire.createNewFile();
			if(fire.length()>1024*1024*50){//大于50M则自动删除
				fire.delete();
				fire.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(fire,true);
			fos.write("\n-----------------------\n".getBytes());
			fos.write(sb.toString().getBytes());
			fos.write("\n-----------------------\n".getBytes());
			fos.flush();
			fos.close();
			//}
		} catch (Exception e) {
		}
	}

	/**
	 * 将异常包入到文件
	 * @param ex
	 */
	public void saveCrashInfo2File(Context context,Throwable ex) {
		final Map<String,String> map=new HashMap<String,String>();
		SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("exceptionTime=", formate.format(Calendar.getInstance().getTime()));
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			//if (BaseApplication.hasSDcard()){
			String dir = SDCardUtil.getInstance(context).getDebugPath();
			File fileDirectroy=new File(dir);
			if(!fileDirectroy.exists()){
				fileDirectroy.mkdir();
			}
			File fire = new File(fileDirectroy,"log.txt");
			if(!fire.exists()) fire.createNewFile();
			if(fire.length()>1024*1024*50){//大于50M则自动删除
				fire.delete();
				fire.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(fire,true);
			fos.write(sb.toString().getBytes());
			fos.write("\n-----------------------\n".getBytes());
			fos.flush();
			fos.close();
			//}
		} catch (Exception e) {
		}
	}
}