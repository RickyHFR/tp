---
layout: page
title: User Guide
---

FinClient is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, FinClient can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------
## Quick start {#quick-start}

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2425S2-CS2103T-T11-4/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your FinClient.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar finclient.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------
--- 
layout: page
title: Introduction
---
## Features {#features}

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help` 

Shows a message explaning how to access the help page.

![help message](images/helpMsg.png)

Format: `help`


### Adding a person: `add`

Adds a person to FinClient.

Format: `add n/NAME p/PHONE_NUMBER [p/PHONE_NUMBER] [p/PHONE_NUMBER] e/EMAIL a/ADDRESS r/REMARKS [t/TAG]…​ `

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 r/10 year sentence t/criminal `

### Listing all persons : `list`

Shows a list of all persons in FinClient.

Format: `list`

### Editing a person : `edit`

Edits an existing person in FinClient.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [r/REMARK] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a person : `delete`

Deletes the specified person from FinClient.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in FinClient.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Hiding a person : `hide`

Hides the details of the specified person in FinClient.

Format: `hide all|INDEX|name`

* Hides the details of the person specified at `INDEX` or by `name`. 
* Hides all contacts' details if `all` is used.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `hide 2` hides the 2nd person in FinClient.
* `find Betsy` followed by `hide 1` hides the 1st person in the results of the `find` command.

### Revealing a person : `reveal`

Reveals the details of the specified person in FinClient.

Format: `reveal all|INDEX|name`

* Reveals the details of the person specified at `INDEX` or by `name`.
* Reveals all contacts' details if `all` is used.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `reveal 2` reveals the 2nd person in FinClient.
* `find Betsy` followed by `reveal 1` reveals the 1st person in the results of the `find` command.

### Adding remarks: `remark`

Adds a remark to the specified person in FinClient.

Format: `remark INDEX r/[REMARKS]`

* Adds a remark to the person specified at `INDEX`

Examples:
* `remark 1 r/this is a test remark` adds `this is a test remark` to the remark section of the contact listed at index 1

### Sorting contacts: `sort`

Sorts the contact list based on the criteria of name or phone number.

Format: `sort c/CRITERIA`

Examples:
* `sort c/name` sorts the contact list in FinClient based on contact's name
* `sort c/phone` sorts the contact list in FinClient based on contact's first phone number

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

FinClient data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

FinClient data are saved automatically as a JSON file `[JAR file location]/data/finclient.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, FinClient will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause FinClient to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ {#faq}

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous FinClient home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues {#known-issues}

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary {#command-summary}

 Action     | Format, Examples                                                                                                                                                                                 
------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 **Add**    | `add n/NAME p/PHONE_NUMBER [p/PHONE_NUMBER] [p/PHONE_NUMBER] e/EMAIL a/ADDRESS r/REMARK [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague` 
 **Clear**  | `clear`                                                                                                                                                                                          
 **Delete** | `delete INDEX`<br> e.g., `delete 3`                                                                                                                                                              
 **Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [r/REMARK] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                                                      
 **Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                                                       
 **Hide**   | `hide all` or `hide INDEX` or `hide name`                                                                                                                                                                                                |INDEX|name`
 **Reveal** | `reveal all` or `reveal INDEX` or `reveal name`  
 **Sort**   | `sort`
 **List**   | `list`                                                                                                                                                                                           
 **Help**   | `help`                                                                                                                                                                                           
