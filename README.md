# StockChecker
This project takes a list of stock symbols and will track the physical locations for each company.

I realized that I will not be able to track every single location of every single company because of the logistics, so I decided to track each store within a certain radius of the top two cities in each state for the given companies. The information for all of this is stored in the resources directory, and used in the main java directory.

If you would like to run this, you will need your own google api key and then change ```DummyKeys.java``` to hold your keys and database driver information. Then, each class that implements Keys has to implement your keys class.

This is a reupload of the repository, because it came to my attention that keys can be decompiled from the gradle build.