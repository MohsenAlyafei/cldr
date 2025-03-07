package org.unicode.cldr.unittest;

import java.util.EnumSet;

import org.unicode.cldr.util.personname.PersonNameFormatter;
import org.unicode.cldr.util.personname.PersonNameFormatter.Field;
import org.unicode.cldr.util.personname.PersonNameFormatter.FormatParameters;
import org.unicode.cldr.util.personname.PersonNameFormatter.Length;
import org.unicode.cldr.util.personname.PersonNameFormatter.ModifiedField;
import org.unicode.cldr.util.personname.PersonNameFormatter.Modifier;
import org.unicode.cldr.util.personname.PersonNameFormatter.NameObject;
import org.unicode.cldr.util.personname.PersonNameFormatter.NamePattern;
import org.unicode.cldr.util.personname.PersonNameFormatter.NamePatternData;
import org.unicode.cldr.util.personname.PersonNameFormatter.Order;
import org.unicode.cldr.util.personname.PersonNameFormatter.ParameterMatcher;
import org.unicode.cldr.util.personname.PersonNameFormatter.Style;
import org.unicode.cldr.util.personname.PersonNameFormatter.Usage;
import org.unicode.cldr.util.personname.SimpleNameObject;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.ibm.icu.dev.test.TestFmwk;
import com.ibm.icu.util.ULocale;

public class TestPersonNameFormatter extends TestFmwk{

    public static void main(String[] args) {
        new TestPersonNameFormatter().run(args);
    }

    /**
     * TODO flesh out and add more tests as the engine adds functionality
     */

    public void TestBasic() {

        // TODO make a convenience function ParameterMatcher.from() that takes a single string and parses, for ease of testing

        ParameterMatcher nameRuleParameters1 = new ParameterMatcher(
            EnumSet.of(Length.medium, Length.narrow),
            EnumSet.of(Style.formal),
            EnumSet.of(Usage.addressing),
            null);
        ImmutableMap<ULocale, Order> localeToOrder = ImmutableMap.of(); // don't worry about using the order from the locale right now.

        // TODO Make a version of NamePattern.from() that takes a single string and parses, for ease of testing
        // TOOD For each example in the spec, make a test case for it.

        NamePatternData namePatternData = new NamePatternData(
            localeToOrder,
            ImmutableListMultimap.of(
                nameRuleParameters1, NamePattern.from("{given}", " ", "{given2-initial}", ". ", "{surname}"),
                nameRuleParameters1, NamePattern.from("{given}", " ", "{surname}"),
                ParameterMatcher.MATCH_ALL, NamePattern.from("{given}", " ", "{given2}", " ", "{surname}")
                ));
        PersonNameFormatter personNameFormatter = new PersonNameFormatter(namePatternData);

        // TODO make a SimpleNameObject.from() that takes a single string and parses, for ease of testing

        NameObject nameObject = new SimpleNameObject(ULocale.FRENCH, ImmutableMap.of(
            new ModifiedField(Field.given), "John",
            new ModifiedField(Field.given2, Modifier.initial), "B",
            new ModifiedField(Field.given2), "Bob",
            new ModifiedField(Field.surname), "Smith"));

        FormatParameters formatParameters1 = FormatParameters.from("length=medium style=formal usage=addressing");
        FormatParameters formatParameters2 = FormatParameters.from("length=long style=formal usage=addressing");

        check(personNameFormatter, nameObject, formatParameters1, "John B. Smith");
        check(personNameFormatter, nameObject, formatParameters2, "John Bob Smith");
    }

    private void check(PersonNameFormatter personNameFormatter, NameObject nameObject, FormatParameters nameFormatParameters1, String expected) {
        String actual = personNameFormatter.format(nameObject, nameFormatParameters1);
        assertEquals("\n\t\t" + personNameFormatter + ";\n\t\t" + nameObject + ";\n\t\t" + nameFormatParameters1.toString(), expected, actual);
    }
}
