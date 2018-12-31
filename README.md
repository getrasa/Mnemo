# Mnemo
![Mnemo Printscreen](/printscreen.PNG)


### Description (This application is still incomplete!)
Mnemo is an SRS application based on spaced repetition method, which is a learning technique that incorporates increasing intervals of time between subsequent review of previously learned material in order to exploit the psychological spacing effect ([Link](https://en.wikipedia.org/wiki/Spaced_repetition)). The purpose of this application is to both enhance and simplify the process of memorizing new vocabulary. 

The name *Mnemo* is derived from Ancient Greek word "mnemo" which means "memory". Additionally it can also be thought of as an abreviation to the word "mnemonic".

#### Intervals
The intervals range from 4hours to 4months each assigning particular level to a word. If a word has been reviewed correctly at a given interval, its level will go one up, or one down otherwise. When a word with level 8 is reviewed correctly it is considered as "mnemorized" and will not appear again in the cue.
- Lvl 1 | 4h
- Lvl 2 | 8h
- Lvl 3 | 23h   (1d - 1h)
- Lvl 4 | 47h   (2d - 1h)
- Lvl 5 | 167h  (1w - 1h)
- Lvl 6 | 335h  (2w - 1h)
- Lvl 7 | 719h  (1m - 1h)
- Lvl 8 | 2879h (4m - 1h)



### Tools
Mnemo has been designed in Adobe xD. The language used is Java, with help of JavaFX library, all wrapped up in Maven (Project Management Tool). As for the database language I used Sqlite which was added as a dependency to Maven. 


#### Designed and programmed by Damian Matkowski
