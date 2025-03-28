package seedu.finclient.testutil;

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

import java.util.Set;

import seedu.finclient.logic.commands.AddCommand;
import seedu.finclient.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.finclient.model.person.Person;
import seedu.finclient.model.person.Phone;
import seedu.finclient.model.tag.Tag;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME).append(person.getName().fullName).append(" ");

        for (Phone phone : person.getPhoneList().phoneList) {
            sb.append(PREFIX_PHONE).append(phone.value).append(" ");
        }

        sb.append(PREFIX_EMAIL).append(person.getEmail().value).append(" ");
        sb.append(PREFIX_ADDRESS).append(person.getAddress().value).append(" ");
        sb.append(PREFIX_REMARK + person.getRemark().value + " ");

        person.getTags().stream().forEach(
                s -> sb.append(PREFIX_TAG).append(s.tagName).append(" ")
        );

        sb.append(PREFIX_COMPANY).append(person.getCompany().value).append(" ");
        sb.append(PREFIX_JOB).append(person.getJob().value).append(" ");
        sb.append(PREFIX_PLATFORM).append(person.getStockPlatform().value).append(" ");
        sb.append(PREFIX_NETWORTH).append(person.getNetworth().value).append(" ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));

        descriptor.getPhoneList().ifPresent(phoneList -> {
            for (Phone phone : phoneList.phoneList) {
                sb.append(PREFIX_PHONE).append(phone.value).append(" ");
            }
        });

        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        descriptor.getRemark().ifPresent(remark -> sb.append(PREFIX_REMARK).append(remark.value).append(" "));
        descriptor.getCompany().ifPresent(company -> sb.append(PREFIX_COMPANY).append(company.value).append(" "));
        descriptor.getJob().ifPresent(job -> sb.append(PREFIX_JOB).append(job.value).append(" "));
        descriptor.getStockPlatform().ifPresent(platform -> sb
                .append(PREFIX_PLATFORM).append(platform.value).append(" "));
        descriptor.getNetworth().ifPresent(worth -> sb.append(PREFIX_NETWORTH).append(worth.value).append(" "));

        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }

        return sb.toString();
    }
}
