package net.apnic.rdap.conformance.attributetest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Locale;

import com.google.common.collect.Sets;

import net.apnic.rdap.conformance.Result;
import net.apnic.rdap.conformance.Context;
import net.apnic.rdap.conformance.Utils;
import net.apnic.rdap.conformance.AttributeTest;

public class Country implements AttributeTest
{
    private static final Set<String> countryCodes = new HashSet<String>();
    static {
        String[] arrayCountryCodes = Locale.getISOCountries();
        for (String countryCode : arrayCountryCodes) {
            countryCodes.add(countryCode);
        }
    }

    public Country() {}

    public boolean run(Context context, Result proto,
                       Map<String, Object> data)
    {
        Result nr = new Result(proto);
        nr.setCode("content");
        nr.addNode("country");
        nr.setDocument("draft-ietf-weirds-json-response-06");
        nr.setReference("4");

        String countryValue =
            Utils.getStringAttribute(context, nr, "country",
                                     Result.Status.Notification,
                                     data);
        if (countryValue == null) {
            return false;
        }

        Result nr3 = new Result(nr);
        boolean res = nr3.setDetails(countryCodes.contains(countryValue),
                                     "valid",
                                     "invalid: " + countryValue);
        context.addResult(nr3);

        return res;
    }

    public Set<String> getKnownAttributes()
    {
        return Sets.newHashSet("country");
    }
}
