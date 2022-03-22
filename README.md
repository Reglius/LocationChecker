# StockChecker
This project takes a list of stock symbols and will track the physical locations for each company.

I decided to track each store within a certain radius of the top two cities in each state for the given companies. The information for all of this is stored in the resources directory, and used in the main java directory.

If you would like to run this, you will need your own google api key and then change ```DummyKeys.java``` to hold your keys and database driver information. Then, each class that implements Keys has to implement your keys class.

Here is a sample output
+------+------------+------+
| NAME | DATE       | LOC  |
+------+------------+------+
| ANF  | 2021-08-10 | 2932 |
| AEO  | 2021-08-10 | 1951 |
| GPS  | 2021-08-10 | 4937 |
| JWN  | 2021-08-10 | 3903 |
| RL   | 2021-08-10 | 2286 |
| URBN | 2021-08-10 | 4027 |
| M    | 2021-08-10 | 4035 |
+------+------------+------+
