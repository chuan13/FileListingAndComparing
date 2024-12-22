# 檔案列表與比對工具
專案分類：個人向  
開發中……  

<br>

## 如何使用（目前）
### 在 IDE 中執行
- 在 IDE 修改 Main.java 程式碼，輸入功能與目標資料夾路徑  

#### 比對
- ``` Java
  String mode = "compare";
  String dir1PathString = "（比對目標資料夾 1 的絕對路徑）";
  String dir2PathString = "（比對目標資料夾 2 的絕對路徑）";
  ```
- 結果
  - 輸出於執行路徑下的「FileCompareOutput.txt」

<br>

## 待開發
- 主要功能
  - 完成「列表功能」
- 使用
  - CLI 傳入參數啟動
  - GUI（Swing）
- 打包
  - 打包為 jar 檔
  - 打包為 exe 檔
- 優化
  - 「比對功能」除了比對檔案名稱，也比對修改日期與檔案大小
  - 整理程式碼
    - 錯誤處理
    - 把輸出邏輯交給另一個執行緒
