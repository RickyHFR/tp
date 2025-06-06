package seedu.finclient.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_JOB;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_NETWORTH;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_PLATFORM;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.finclient.logic.parser.CliSyntax.PREFIX_TIMESTAMP;
import static seedu.finclient.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.finclient.commons.core.index.Index;
import seedu.finclient.commons.util.CollectionUtil;
import seedu.finclient.commons.util.ToStringBuilder;
import seedu.finclient.logic.Messages;
import seedu.finclient.logic.commands.exceptions.CommandException;
import seedu.finclient.model.Model;
import seedu.finclient.model.order.Order;
import seedu.finclient.model.person.Address;
import seedu.finclient.model.person.Company;
import seedu.finclient.model.person.Email;
import seedu.finclient.model.person.Job;
import seedu.finclient.model.person.Name;
import seedu.finclient.model.person.Networth;
import seedu.finclient.model.person.Person;
import seedu.finclient.model.person.PhoneList;
import seedu.finclient.model.person.Remark;
import seedu.finclient.model.person.StockPlatform;
import seedu.finclient.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_REMARK + "REMARK "
            + "[" + PREFIX_TIMESTAMP + "TIMESTAMP]] "
            + "[" + PREFIX_COMPANY + "COMPANY] "
            + "[" + PREFIX_JOB + "JOB] "
            + "[" + PREFIX_PLATFORM + "STOCKPLATFORM] "
            + "[" + PREFIX_NETWORTH + "NETWORTH] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_PERSON_HIDDEN = "This person is hidden. Reveal the person before editing.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (personToEdit.getIsHidden()) {
            throw new CommandException(MESSAGE_PERSON_HIDDEN);
        }

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        PhoneList updatedPhoneList = editPersonDescriptor.getPhoneList().orElse(personToEdit.getPhoneList());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Order updatedOrder = editPersonDescriptor.getOrder().orElse(personToEdit.getOrder());
        Remark updatedRemark = editPersonDescriptor.getRemark().orElse(personToEdit.getRemark());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Company updatedCompany = editPersonDescriptor.getCompany().orElse(personToEdit.getCompany());
        Job updatedJob = editPersonDescriptor.getJob().orElse(personToEdit.getJob());
        StockPlatform updatedStockPlatform =
                editPersonDescriptor.getStockPlatform().orElse(personToEdit.getStockPlatform());
        Networth updatedNetworth = editPersonDescriptor.getNetworth().orElse(personToEdit.getNetworth());

        return new Person(updatedName, updatedPhoneList, updatedEmail, updatedAddress, updatedOrder, updatedRemark,
                updatedTags, updatedCompany, updatedJob, updatedStockPlatform, updatedNetworth);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private PhoneList phoneList;
        private Email email;
        private Address address;
        private Order order;
        private Remark remark;
        private Set<Tag> tags;
        private Company company;
        private Job job;
        private StockPlatform stockPlatform;
        private Networth networth;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhoneList(toCopy.phoneList);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setOrder(toCopy.order);
            setRemark(toCopy.remark);
            setTags(toCopy.tags);
            setCompany(toCopy.company);
            setJob(toCopy.job);
            setStockPlatform(toCopy.stockPlatform);
            setNetworth(toCopy.networth);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phoneList, email, address, remark, tags, company, job,
                    stockPlatform, networth);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhoneList(PhoneList phoneList) {
            this.phoneList = phoneList;
        }

        public Optional<PhoneList> getPhoneList() {
            return Optional.ofNullable(phoneList);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public Optional<Order> getOrder() {
            return Optional.ofNullable(order);
        }

        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }
        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public void setCompany(Company company) {
            if (company != null && company.value.equals("delete")) {
                this.company = new Company();
            } else {
                this.company = company;
            }
        }

        public Optional<Company> getCompany() {
            return Optional.ofNullable(company);
        }

        public void setJob(Job job) {
            if (job != null && job.value.equals("delete")) {
                this.job = new Job();
            } else {
                this.job = job;
            }
        }

        public Optional<Job> getJob() {
            return Optional.ofNullable(job);
        }

        public void setStockPlatform(StockPlatform stockPlatform) {
            if (stockPlatform != null && stockPlatform.value.equals("delete")) {
                this.stockPlatform = new StockPlatform();
            } else {
                this.stockPlatform = stockPlatform;
            }
        }

        public Optional<StockPlatform> getStockPlatform() {
            return Optional.ofNullable(stockPlatform);
        }

        public void setNetworth(Networth networth) {
            if (networth != null && networth.value.equals("delete")) {
                this.networth = new Networth();
            } else {
                this.networth = networth;
            }
        }

        public Optional<Networth> getNetworth() {
            return Optional.ofNullable(networth);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phoneList, otherEditPersonDescriptor.phoneList)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(order, otherEditPersonDescriptor.order)
                    && Objects.equals(remark, otherEditPersonDescriptor.remark)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(company, otherEditPersonDescriptor.company)
                    && Objects.equals(job, otherEditPersonDescriptor.job)
                    && Objects.equals(stockPlatform, otherEditPersonDescriptor.stockPlatform)
                    && Objects.equals(networth, otherEditPersonDescriptor.networth);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phones", phoneList)
                    .add("email", email)
                    .add("address", address)
                    .add("order", order)
                    .add("remark", remark)
                    .add("tags", tags)
                    .add("company", company)
                    .add("job", job)
                    .add("stockPlatform", stockPlatform)
                    .add("networth", networth)
                    .toString();
        }
    }
}
