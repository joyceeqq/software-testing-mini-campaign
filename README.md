# software-testing-mini-campaign
### Author
Joyce Lim 1005307
## Task
Consider a CSV file that stores a list of records (e.g., records of bank accounts). You are required to write a software program that reads two such CSV files, compares records stored in these CSV files row by row against a unique combination and records all mismatches as exceptions. Finally, the software program generates another csv file listing the exceptions.
## Example (for better understanding)
Consider customer files that contain customer id, customer account number, currency, account type (e.g., savings/current) and available balance. Compare available balance against a unique combination of customer id, customer account number, account type and currency. Generate a CSV file with records from both the files and corresponding to the mismatched amount (i.e., balance) for the unique combination.
## Before running the project...
Make sure you have java installed. Instructions for installing java on Mac can be found here: https://www.javatpoint.com/how-to-install-java-on-mac while windows can be found here: https://phoenixnap.com/kb/install-java-windows
## Running the project
1. Git clone this project by running the following code in your terminal
```
git clone https://github.com/joyceeqq/software-testing-mini-campaign.git
```
2. Place the 2 CSV files you would want to compare into the `/sample` directory. You can also run this program with the 3 sample csv files provided in the directory.
3. Key in this 
```
javac Main.java
```
4. To compare 2 csv files, please follow the format of: `java Main {filename1} {filename2} --columns {columns to compare that's separated by comma}`. The following is an example to compare sample_file_1.csv and sample_file_2.csv.
```
java Main sample_file_1.csv sample_file_3.csv --columns "Customer ID#","Account No.","Type"
```
5. The csv file containing all mismatch and excpetions will then be generated under the exception folder as "output.csv"