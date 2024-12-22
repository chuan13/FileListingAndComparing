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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Comparing {
	private Util util;

	public Comparing(Util util) {
		this.util = util;
	}
	
	public void compare(String dir1PathString, String dir2PathString) throws IOException, IsNotDirectoryException {
		File dir1 = new File(dir1PathString);
		File dir2 = new File(dir2PathString);
		
		// 錯誤判別
		if (!dir1.isDirectory() || !dir2.isDirectory()) {
			throw new IsNotDirectoryException();
		}
		
		List<String> allFilesRelativePathString1 = util.findAllDirAndFileRelativePathString(dir1);
		List<String> allFilesRelativePathString2 = util.findAllDirAndFileRelativePathString(dir2);
		
		Map<List<String>, List<String>> resultMap = listsCompare(allFilesRelativePathString1, allFilesRelativePathString2);
		Entry<List<String>, List<String>> entry = resultMap.entrySet().iterator().next();
		List<String> resultList1 = entry.getKey();
		List<String> resultList2 = entry.getValue();
		
		outputToTxt(dir1, resultList1, dir2, resultList2);
		System.out.println("執行結束。");
	}
	
	/**
	 * 比較兩個 List<String> 的內容（相對路徑），去除兩邊共有的元素；<br>
	 * 如果有整個資料夾不共有，也去除資料夾內容、只留資料夾本身。
	 * 
	 * @param list1
	 * @param list2
	 * @return key 為整理後的 List1，value 為整理後的 List2。
	 */
	private Map<List<String>, List<String>> listsCompare(List<String> list1, List<String> list2) {
		Map<List<String>, List<String>> sourceResultMap = util.compareAndRemoveBetweenList(list1, list2);
		Entry<List<String>, List<String>> sourceResultEntry = sourceResultMap.entrySet().iterator().next();
		List<String> resultList1 = removeInDirectory(sourceResultEntry.getKey());
		List<String> resultList2 = removeInDirectory(sourceResultEntry.getValue());
		
		Map<List<String>, List<String>> resultMap = new HashMap<List<String>, List<String>>();
		resultMap.put(resultList1, resultList2);
		return resultMap;
	}
	
	/**
	 * 去除資料夾內容、只留資料夾本身。
	 * 
	 * @param list
	 * @return
	 */
	private static List<String> removeInDirectory(List<String> list) {
		// 目前檢查中的資料夾
		String temp = null;
		// 由於「同個資料夾下的檔案是連續的」，temp 目前有內容就代表目前都在此資料夾內、直到 contains() 為 false。
		
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String element = iterator.next();
			
			if (temp == null) {  // 不在不共有資料夾內
				if ("\\".equals(element.substring(element.length() - 1))) {  // 是資料夾					
					temp = element;  // 開始檢查與去除
				}
			} else {  // 在檢查中資料夾內
				if (element.contains(temp)) {  // 還在檢查中資料夾內
					iterator.remove();
				} else {  // 已經離開檢查中資料夾
					temp = null;
					
					// 此輪已略過資料夾檢查，重新檢查
					if ("\\".equals(element.substring(element.length() - 1))) {  // 是資料夾					
						temp = element;  // 開始檢查與去除
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 將比較結果輸出至目前執行目錄下的「FileCompareOutput.txt」。
	 * 
	 * @param contentStringList
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void outputToTxt(File dir1, List<String> list1, File dir2, List<String> list2) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		File outputFile = new File(System.getProperty("user.dir") + File.separator + "FileCompareOutput.txt");
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
			// 首先寫入輸出資訊
			writer.write("輸出時間：" + LocalDateTime.now());
			writer.newLine();
			writer.write("比較基準資料夾：" + dir1.getAbsolutePath());
			writer.newLine();
			writer.write("比較目標資料夾：" + dir2.getAbsolutePath());
			writer.newLine();
			
			
			// list1
			
			// 先寫入第一行
			writer.write("+ " + list1.get(0));
			
			// 寫入之後的內容
			for (int i = 1; i < list1.size(); i++) {
				writer.newLine();
				writer.write("+ " + list1.get(i));
			}
			
			
			// list2
			
			// 寫入內容
			for (String string : list2) {
				writer.newLine();
				writer.write("- " + string);
			}
		}
	}
	
}
