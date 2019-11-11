package test.change;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import info.monitorenter.cpdetector.CharsetPrinter;

public class GBKToUTF8 {
	public static String DefaultSrcEncodeFormat = "GBK";
	public static String DefaultDestEncodeFormat = "UTF-8";
	public static String UnsupportedEncodingExceptionError = "编码格式错误！";
	public static String FileNotFoundExceptionError = "文件不存在！";
	public static String IOExceptionError = "文件读写错误！";
	public static String IsUtf8File = "文件是UTF-8编码格式！";
	public static String IsNotUtf8File = "文件不是UTF-8编码格式！";
	public static CharsetPrinter detector = new CharsetPrinter();

	// 遍历目录，将文件从GBK转换成UTF-8
	public static void fileList(File file,List<String> chgList) {
		File rootFile = file;
		File[] files = rootFile.listFiles();
		if (files != null) {
			for (File f : files) {
				String fileName = f.getName();
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (!f.isDirectory() && "java".equals(suffix)) {
					codeConvert(f,chgList);
				}
				fileList(f,chgList);// 递归调用子文件夹下的文件
			}
		}
	}

	public static void main(String[] args) {
		List<String> chgList = new ArrayList<String>();
		// 要转换的目录--目标资源URL
		String targetFileUrl = "E:\\";
		File file = new File(targetFileUrl);
		GBKToUTF8.fileList(file,chgList);
		System.out.println("--------------------------------");
		System.out.println("转换的文件目录是"+file.getPath());
		System.out.println("--------------------------------");
		System.out.println("转换的文件列表：");
		for (int i = 0; i < chgList.size(); i++) {
			System.out.println(chgList.get(i).toString());
		}
		System.out.println("--------------------------------");
	}

	public static void codeConvert(File file,List<String> chgListLog) {
		try {
			if (isUTF8File(file)) {
				System.err.println(file.getName()+"是UTF-8编码,跳过处理.");
				return;
			}
			chgListLog.add("guessFileEncode:" + detector.guessEncoding(file)+" | 文件:"+file.getPath());
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), Charset.forName("GBK")));
			StringBuilder sb = new StringBuilder();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("\n");
			}
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
			bw.write(sb.toString());
			bw.flush();
			bw.close();
			// br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isUTF8File(File file) {
		try {
			CharsetPrinter detector = new CharsetPrinter();
			String charset = detector.guessEncoding(file);
			if (charset.equalsIgnoreCase(DefaultDestEncodeFormat)) {
//				System.out.println(IsUtf8File);
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(FileNotFoundExceptionError);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(IOExceptionError);
		}

//		System.out.println(IsNotUtf8File);
		return false;

	}
}
