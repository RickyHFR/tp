---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# FinClient Developer Guide

This document provides a guide for developers who want to contribute to the project.

## Table of contents

- [Acknowledgements](#acknowledgements)
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI component](#ui-component)
    - [Logic component](#logic-component)
    - [Model component](#model-component)
    - [Storage component](#storage-component)
    - [Common classes](#common-classes)
- [Implementation](#implementation)
    - [Order function and Call Auction Calculator](#order-function-and-call-auction-calculator)
    - [Accessing Help Message](#accessing-help-message)
    - [Proposed: Undo/redo feature](#proposed-undo-redo-feature)
- [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
- [Appendix: Requirements](#appendix-requirements)
    - [Product scope](#product-scope)
    - [User stories](#user-stories)
    - [Use cases](#use-cases)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
- [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* [Code](https://github.com/se-edu/addressbook-level4) from [AB4](https://se-education.org/addressbook-level4/), a SE-EDU project, has been reused and adapted in FinClient for GUI testing purposes.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture
<div class="text-center">
  <puml src="diagrams/ArchitectureDiagram.puml" width="300"></puml>
  <figcaption><strong>Figure 1:</strong> Architecture Diagram.</figcaption>
</div>
<br>

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

<div class="text-center">
  <puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574"></puml>
  <figcaption><strong>Figure 2:</strong> Architecture Sequence Diagram.</figcaption>
</div>
<br>

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<div class="text-center">
  <puml src="diagrams/ComponentManagers.puml" width="300"></puml>
  <figcaption><strong>Figure 3:</strong> Component Managers Diagram.</figcaption>
</div>
<br>

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/ui/Ui.java)

<div class="text-center">
  <puml src="diagrams/UiClassDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 4:</strong> UI Class Diagram.</figcaption>
</div>
<br>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/logic/Logic.java)

<div class="text-center">
  <puml src="diagrams/LogicClassDiagram.puml" width="600"></puml>
  <figcaption><strong>Figure 5:</strong> Logic Class Diagram.</figcaption>
</div>
<br>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<div class="text-center">
  <puml src="diagrams/DeleteSequenceDiagram.puml" width="600"></puml>
  <figcaption><strong>Figure 6:</strong> Delete Sequence Diagram.</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `FinClientParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<div class="text-center">
  <puml src="diagrams/ParserClasses.puml" width="600"></puml>
  <figcaption><strong>Figure 7:</strong> Parser Classes Diagram.</figcaption>
</div>
<br>

How the parsing works:
* When called upon to parse a user command, the `FinClientParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `FinClientParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/model/Model.java)

<div class="text-center">
  <puml src="diagrams/ModelClassDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 8:</strong> Model Class Diagram.</figcaption>
</div>
<br>

The `Model` component,

* stores the finclient data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">

:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `FinClient`, which `Person` references. This allows `FinClient` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<div class="text-center">
  <puml src="diagrams/BetterModelClassDiagram.puml" width="450"></puml>
  <figcaption><strong>Figure 9:</strong> Better Model Class Diagram.</figcaption>
</div>

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/storage/Storage.java)

<div class="text-center">
  <puml src="diagrams/StorageClassDiagram.puml" width="550"></puml>
  <figcaption><strong>Figure 10:</strong> Storage Class Diagram.</figcaption>
</div>
<br>

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `FinClientStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.finclient.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Order function and Call Auction Calculator

This feature extends the Person class by adding an order field to record the amount and price for each client’s order. Additionally, a call auction mechanism is integrated within the Model to compute a clearing price based on the complete order book.


#### Implementation

Order Update:

When a user issues an `order` command, the application updates the corresponding `Person` object in the `Model` with the new order details. This update is handled by the `OrderCommand`, which modifies the Model accordingly.

Call Auction Calculator Integration:

The Call Auction Calculator is embedded within the `Model`. All orders are maintained in the `Model`, and the `Model` exposes a method, `model#calculateClearingPrice()`, which utilizes the internal `CallAuctionCalculator` to aggregate the order data and compute the clearing price. The computation determines the price level at which the maximum transaction volume is achieved.

Clearing Price Query:

The clearing price is retrieved on demand by the `UI`. When needed, the `UI` calls `Logic#getClearingPrice()`, which in turn calls `Model#calculateClearingPrice()` and returns the computed clearing price back to the `UI` for display.

<div class="text-center">
  <puml src="diagrams/OrderSequenceDiagram.puml" width="1000"></puml>
  <figcaption><strong>Figure 11:</strong> Order Sequence Diagram.</figcaption>
</div>
<br>

#### Design Considerations

* **Separation of Concerns:**
The `order` command is solely responsible for updating the `Model`, while the clearing price calculation is a distinct operation invoked by the `UI`. This separation ensures that the processes for updating orders and calculating the auction clearing price remain decoupled.
* **Data Consistency:**
All order data is maintained within the `Model`. The integrated call auction calculator accesses the current order book directly from the Model, ensuring that the most up-to-date information is used for computing the clearing price.
* **Performance:**
The clearing price computation is optimized to handle a large volume of orders efficiently. Future enhancements might include caching strategies to further improve performance.
* **Extensibility:**
The `CallAuctionCalculator` is implemented as a modular component within the Model. This design allows for easy modifications or extensions of the auction mechanism without impacting other parts of the system.
* **Error Handling:**
If the order data is incomplete or invalid, the `Logic` will trigger appropriate error messages during the order parsing phase. The clearing price is computed only when the Model’s state is consistent, ensuring accurate auction results.

### Accessing Help Message
The feature allows the user to obtain relevant information regarding the commands available while also giving them the choice to copy a URL to our User Guide when scrolled to the bottom of the help message.

#### Implementation

Accessing Help:

When a user issues a `help` command, or presses a shortcut of `Fn + F1`, the application will run the command through the `Logic` component which then interacts with the `UI` components of `MainWindow` and `HelpWindow`.

Closing Help:

When a user has finished using the help function, the user can click on the `X` to close it or alternatively, press on the `ESCAPE` key to close the help message. This action is registered via the `HelpWindow`.

<div class="text-center">
  <puml src="diagrams/HelpSequenceDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 12:</strong> Help Sequence Diagram.</figcaption>
</div>
<br>

<div class="text-center">
  <puml src="diagrams/HelpActivityDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 13:</strong> Help Activity Diagram.</figcaption>
</div>
<br>

### \[Proposed\] Undo\/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedFinClient`. It extends `FinClient` with an undo/redo history, stored internally as an `FinClientStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedFinClient#commit()` — Saves the current address book state in its history.
* `VersionedFinClient#undo()` — Restores the previous address book state from its history.
* `VersionedFinClient#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitFinClient()`, `Model#undoFinClient()` and `Model#redoFinClient()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedFinClient` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<div class="text-center">
  <puml src="diagrams/UndoRedoState0.puml" width="500"></puml>
  <figcaption><strong>Figure 14:</strong> UndoRedo State 0.</figcaption>
</div>
<br>

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitFinClient()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `FinClientStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<div class="text-center">
  <puml src="diagrams/UndoRedoState1.puml" width="500"></puml>
  <figcaption><strong>Figure 15:</strong> UndoRedo State 1.</figcaption>
</div>
<br>

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitFinClient()`, causing another modified address book state to be saved into the `FinClientStateList`.

<div class="text-center">
  <puml src="diagrams/UndoRedoState2.puml" width="500"></puml>
  <figcaption><strong>Figure 16:</strong> UndoRedo State 2.</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** If a command fails its execution, it will not call `Model#commitFinClient()`, so the address book state will not be saved into the `FinClientStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoFinClient()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<div class="text-center">
  <puml src="diagrams/UndoRedoState3.puml" width="500"></puml>
  <figcaption><strong>Figure 17:</strong> UndoRedo State 3.</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial FinClient state, then there are no previous FinClient states to restore. The `undo` command uses `Model#canUndoFinClient()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<div class="text-center">
  <img src="images/UndoSequenceDiagram-Logic.png" alt="Undo Sequence Diagram (Logic)" />
  <figcaption><strong>Figure 21:</strong> Undo Sequence Diagram (Logic).</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

<div class="text-center">
  <img src="images/UndoSequenceDiagram-Model.png" alt="Undo Sequence Diagram (Model)" />
  <figcaption><strong>Figure 22:</strong> Undo Sequence Diagram (Model).</figcaption>
</div>
<br>

The `redo` command does the opposite — it calls `Model#redoFinClient()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">

:information_source: **Note:** If the `currentStatePointer` is at index `FinClientStateList.size() - 1`, pointing to the latest address book state, then there are no undone FinClient states to restore. The `redo` command uses `Model#canRedoFinClient()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitFinClient()`, `Model#undoFinClient()` or `Model#redoFinClient()`. Thus, the `FinClientStateList` remains unchanged.

<div class="text-center">
  <puml src="diagrams/UndoRedoState4.puml" width="500"></puml>
  <figcaption><strong>Figure 18:</strong> UndoRedo State 4.</figcaption>
</div>
<br>

Step 6. The user executes `clear`, which calls `Model#commitFinClient()`. Since the `currentStatePointer` is not pointing at the end of the `FinClientStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<div class="text-center">
  <puml src="diagrams/UndoRedoState5.puml" width="500"></puml>
  <figcaption><strong>Figure 19:</strong> UndoRedo State 5.</figcaption>
</div>
<br>

The following activity diagram summarizes what happens when a user executes a new command:

<div class="text-center">
  <puml src="diagrams/CommitActivityDiagram.puml" width="250"></puml>
  <figcaption><strong>Figure 20:</strong> Commit Activity Diagram.</figcaption>
</div>
<br>

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* 💼 Financial advisors
* 🖥️ Tech-savvy
* 📇 has a need to manage a significant number of contacts
* 📝 has a need to add notes to keep track of their clients’ preferences
* ⌨️ prefer typing
* 🖱️ is reasonably comfortable using CLI apps

**Value proposition**:

* 🚀 Provide fast access to client details
* ✨ Easy adding of new clients and required data
* ⌨️ Optimized for users who prefer CLI
* 📋 Allows tracking of multiple details such as clients hobbies, preferences, status etc


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                            | So that I can…​                                                        |
|--------|--------------------------------------------|---------------------------------------------------------|------------------------------------------------------------------------|
| `* * *` | new user                                   | see usage instructions                                  | refer to instructions when I forget how to use the App                 |
| `* * *` | user                                       | add a new person                                        | add new clients that I need to manage                                  |
| `* * *` | user                                       | delete a person                                         | remove entries that I no longer need                                   |
| `* * *` | user                                       | read details about my clients                           | I can tell what my clients have                                        |
| `* * *` | user                                       | add notes to a person                                   | record important details about my business dealings with them          |
| `* * *` | user                                       | search for clients contacts                             | I can immediately get the data I require of my client                  |
| `* * *` | user                                       | store multiple phone numbers and an email for a contact | I can reach them through different channels                            |
| `* * *` | user                                       | find a person by name                                   | locate details of persons without having to go through the entire list |
| `* * *` | user                                       | hide private contact details                            | minimize chance of someone else seeing them by accident                |
| `* *`  | user with many persons in the address book | sort persons by name                                    | locate a person easily                                                 |
| `* *`  | user with things to remember               | add deadlines                                           | remember to do something for a client                                  |
| `* *`  | user                                       | add multiple numbers                                    | keep track of all my client's phone numbers                            |
| `* *`  | forgetful user                             | add more fields to the clients                          | remember who is who at a quick glance                                  |
| `*`    | pro user                                   | see buying and selling prices of each client            | keep track of who wants to buy or sell at what price quickly           |
| `*`    | pro user                                   | have shortcuts                                          | do my work even faster                                                 |

### Use cases

(For all use cases below, the **System** is the `FinClient` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a person**

**MSS**

1.  User requests to list persons.
2.  FinClient shows a list of persons.
3.  User requests to delete a specific person in the list.
4.  FinClient deletes the person.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. FinClient shows an error message.

      Use case resumes at step 2.

**Use case: Add a person**

**MSS**

1. User requests to add a person.
2. FinClient prompts for the person’s details.
3. User provides the person’s details.
4. FinClient adds the person.

    Use case ends.

**Extensions**

* 3a. User provides an invalid detail.
    * 3a1. FinClient shows an error message.
      
      Use case resumes at step 2.
    * 3a2. User provides a duplicate detail.
      * 3a2.1. FinClient shows an error message.
        
        Use case resumes at step 2.

* 3b. User provides no phone numbers or invalid numbers.
    * 3b1. FinClient shows an error message.
      
      Use case resumes at step 2.

* 3c. User provides more than the accepted number of 3 phone numbers.
    * 3c1. FinClient shows an error message.
      
      Use case resumes at step 2.

**Use case: Edit a person**

**MSS**

1.  User requests to list persons.
2.  FinClient shows a list of persons.
3.  User requests to edit a specific person in the list.
4.  FinClient edits the person's detail while keeping everything else the same.

   Use case ends.

**Extensions**

* 3a. User provides a prefix without any text.
    * 3a1. FinClient shows an error message.

      Use case resumes at step 2.

* 3b. User wants to remove an optional field.
    * 3b1. User provides prefix and delete option.
    * 3b2. FinClient edits the person's detail to remove the optional field.

      Use case ends.

**Use case: Find a person**

**MSS**

1.  User requests to find a person by name.
2.  FinClient shows the person’s details.

    Use case ends.

**Extensions**

* 2a. The person is not found.

  * 2a1. FinClient shows an error message.
  
    Use case resumes at step 1.

**Use case: Hide a person's details**

**MSS**

1. User requests to hide a person's detail by name.
2. FinClient obscures the person’s details.

   Use case ends.

**Use case: Reveal a person's details**

**MSS**

1. User requests to reveal a person's detail by name.
2. FinClient reveals the person’s details.

   Use case ends.

**Use case: Track orders for a person**

**MSS**

1. User requests to track orders for a specific contact.
2. FinClient updates the contact’s order.
3. FinClient calculates and show the call auction clearing price based on the updated order book.

   Use case ends.

**Extensions**

* 1a. The person is not found.

    * 1a1. FinClient shows an error message.

      Use case ends.

* 1b. No order information is provided.

    * 1b1. FinClient empties given person's order.

      Use case resumes at step 3.

**Use case: Add remarks to a person**

**MSS**

1. User requests to add remarks to a person.
2. FinClient prompts for the remarks.
3. User provides the remarks.

    Use case ends.

**Extensions**

* 3a. User provides an empty remark.

    * 3a1. FinClient shows an error message.

      Use case resumes at step 2.

**Use case: Access help message**

**MSS**
1. User requests to access or open help message.
2. FinClient opens help message.
3. User reads help message.
4. User closes help message.

    Use case ends.

**Extensions**

* 2a. User accidentally minimizes help message.
  * 2a1. User manually opens help message.

    Use case resumes at step 3.

* 2b. User overlays help message.
  * User focuses help message.

    Use case resumes at step 3.


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Stock Platform**: App platform that is used to trade with stocks
* **Index**: Number beside the contact's name that is currently displayed, used to specify which contact is to be modified/deleted
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">

:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch.

   1. Download the jar file and copy into an empty folder.

   1. Open a terminal and navigate to the folder with the downloaded jar file.
   
   1. Run the jar file by typing the command `java -jar finclient.jar` into the terminal.

   1. Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences.

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. Missing data on startup.

   1. Delete finclient.json located in /data/.

   2. Re-launch the app.

    Expected: App should be repopulated with default values and work again.

### Deleting a person

1. Deleting a person while all persons are being shown.

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size).<br>
      Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted data files.

   1. Prerequisite : Requires a copy of working finclient.json.

   1. Test case: Delete finclient.json. <br>
      Expected: App should be repopulated with a copy of sample data and work again, without any presence of old data and a fresh copy of finclient.json is saved.
