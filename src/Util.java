import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {

// 取得目標 File 底下所有檔案資訊；包含資料夾與檔案。

	/**
	 * 取得目標 File 底下所有檔案的 File。<br>
	 * 包含資料夾與檔案。<br>
	 * 不包含自身。
	 * 
	 * @param targetDir
	 * @return 內含目標資料夾底下所有資料夾與檔案的 File。
	 * @throws IsNotDirectoryException 
	 */
	public List<File> findAllDirAndFile(File targetDir) throws IsNotDirectoryException {
		if (!targetDir.isDirectory()) {
			throw new IsNotDirectoryException();
		}
		
		List<File> fileList = new LinkedList<File>();
		
		for (File file : targetDir.listFiles()) {  // 遍歷目標的子檔案
			fileList.add(file);  // 無論目標的子檔案為資料夾或檔案，都要加入 List
			if (file.isDirectory()) {  // 目標的子檔案為資料夾
				fileList.addAll(findAllDirAndFile(file));
			}
		}
		return fileList;
	}
	
	/**
	 * 取得目標 File 底下所有檔案的絕對路徑。<br>
	 * 包含資料夾與檔案。<br>
	 * 不包含自身。
	 * 
	 * @param targetDir
	 * @return 目標資料夾底下所有資料夾與檔案的絕對路徑。
	 * @throws IsNotDirectoryException 
	 */
	public List<String> findAllDirAndFileAbsolutePathString(File targetDir) throws IsNotDirectoryException {
		if (!targetDir.isDirectory()) {
			throw new IsNotDirectoryException();
		}
		
		List<String> dirAndFileAbsolutePathStringList = new LinkedList<String>();

		File[] listFiles = targetDir.listFiles();
		
		// 修 bug
		if (listFiles == null) {
			System.out.println("無讀取權限：" + targetDir.getAbsolutePath());
			return dirAndFileAbsolutePathStringList;
		}
		
		for (File file : listFiles) {  // 遍歷目標的子檔案
			dirAndFileAbsolutePathStringList.add(file.getAbsolutePath() + (file.isDirectory() ? File.separator : ""));  // 無論目標的子檔案為資料夾或檔案，都要加入 List
			if (file.isDirectory()) {  // 目標的子檔案為資料夾
				dirAndFileAbsolutePathStringList.addAll(findAllDirAndFileAbsolutePathString(file));
			}
		}
		return dirAndFileAbsolutePathStringList;
	}
	
	/**
	 * 取得目標 File 底下所有檔案的相對路徑。<br>
	 * 包含資料夾與檔案。<br>
	 * 不包含自身。
	 * 
	 * @param targetDir
	 * @return 目標資料夾底下所有資料夾與檔案的相對路徑。
	 * @throws IsNotDirectoryException 
	 */
	public List<String> findAllDirAndFileRelativePathString(File targetDir) throws IsNotDirectoryException {
		List<String> dirAndFileAbsolutePathStringList = findAllDirAndFileAbsolutePathString(targetDir);
		return absoluteToRelativeWithinParent(targetDir, dirAndFileAbsolutePathStringList);
	}
	

	/**
	 * 將絕對路徑 List 根據輸入的共同父資料夾轉為相對路徑 List。
	 * 
	 * @param targetDir
	 * @param absolutePathStringList
	 * @return 相對路徑 List。
	 */
	private List<String> absoluteToRelativeWithinParent(File targetDir, List<String> absolutePathStringList) {
		int targetDirAbsolutePathLength = targetDir.getAbsolutePath().length();
		return absolutePathStringList.stream()
			.map(element -> element.substring(targetDirAbsolutePathLength + 1))
			.collect(Collectors.toCollection(LinkedList<String>::new));
	}


// 輸出 txt 檔。
	
	/**
	 * 將輸入的 String[] 逐行寫入至目前執行目錄下的「output.txt」。
	 * 
	 * @param contentStringArray
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void outputToTxt(String[] contentStringArray) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		if (contentStringArray.length == 0) {
			System.out.println("錯誤：輸出內容為空。");
			return;
		}
		
		File outputFile = new File(System.getProperty("user.dir") + File.separator + "output.txt");
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
			// 首先寫入輸出資訊
			writer.write("輸出時間：" + LocalDateTime.now());
			writer.newLine();
			
			// 先寫入第一行
			writer.write(contentStringArray[0]);
			
			// 寫入之後的內容
			for (int i = 1; i < contentStringArray.length; i++) {
				writer.newLine();
				writer.write(contentStringArray[i]);
			}
		}
	}
	
	/**
	 * 將輸入的 List<String> 逐行寫入至目前執行目錄下的「output.txt」。
	 * 
	 * @param contentStringList
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void outputToTxt(List<String> contentStringList) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		if (contentStringList.size() == 0) {
			System.out.println("錯誤：輸出內容為空。");
			return;
		}
		
		File outputFile = new File(System.getProperty("user.dir") + File.separator + "output.txt");
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
			// 首先寫入輸出資訊
			writer.write("輸出時間：" + LocalDateTime.now());
			writer.newLine();
			
			// 先寫入第一行
			writer.write(contentStringList.get(0));
			
			// 寫入之後的內容
			for (int i = 1; i < contentStringList.size(); i++) {
				writer.newLine();
				writer.write(contentStringList.get(i));
			}
		}
	}
	
	
// 比較兩個 List<String> 的內容，去除兩邊共有的元素。
	
	/**
	 * 比較兩個 List<String> 的內容，去除兩邊共有的元素。<br>
	 * 未做檢查，請先確保兩個 List 都是不重複的。
	 * 
	 * @param list1
	 * @param list2
	 * @return key 為整理後的 List1，value 為整理後的 List2。
	 */
	public Map<List<String>, List<String>> compareAndRemoveBetweenList(List<String> list1, List<String> list2) {
		Iterator<String> iterator1 = list1.iterator();
		while (iterator1.hasNext()) {
			String element = iterator1.next();
			
			int indexOf = list2.indexOf(element);
			if (indexOf != -1) {
				iterator1.remove();
				list2.remove(indexOf);
			}
		}
		Map<List<String>, List<String>> result = new HashMap<List<String>, List<String>>();
		result.put(list1, list2);
		return result;
	}
}
