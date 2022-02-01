# Notes Kata

![Overview Screen](images/Main%20Screen.png)
![Add Screen](images/Add%20screen.png)

## Description

Everyone needs to remember specific things, but we can't remember everything. 
To solve this problem we want to create an app to take notes.

## Requirements

* Overview of all notes in a list
* Add new notes on another screen
* Remove notes from the list
* Persist notes
* Load persisted notes at the start of the app

### Bonus

* Modify existing notes

## Hints

* Start with an activity, which has a fragment-holder to show the different fragments
* Add ViewBinding to the project to easily access the view objects
* Use two fragments, one for presenting the notes and one for adding new notes
* The notes should be shown in a RecyclerView
* The style of the notes will be defined in the ViewHolder
* Remember the Shared Preference Repository, which you already used to persist and load the data

## Resources

* [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
* [Fragments](https://developer.android.com/guide/fragments)
* [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)