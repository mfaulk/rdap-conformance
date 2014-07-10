package net.apnic.rdap.conformance.test.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import net.apnic.rdap.conformance.Result;
import net.apnic.rdap.conformance.Utils;
import net.apnic.rdap.conformance.Result.Status;
import net.apnic.rdap.conformance.Context;
import net.apnic.rdap.conformance.ObjectTest;
import net.apnic.rdap.conformance.attributetest.StandardResponse;
import net.apnic.rdap.conformance.attributetest.Domain;

public class Standard implements ObjectTest
{
    String domain = null;
    String url = null;

    public Standard() {}

    public Standard(String domain)
    {
        this.domain = domain;
    }

    public void setUrl(String url)
    {
        domain = null;
        this.url = url;
    }

    public boolean run(Context context)
    {
        String path =
            (url != null)
                ? url
                : context.getSpecification().getBaseUrl()
                    + "/domain/" + domain;

        Result proto = new Result(Status.Notification, path,
                                  "domain.standard",
                                  "content", "",
                                  "draft-ietf-weirds-json-response-06",
                                  "6.3");
        Result r = new Result(proto);
        r.setCode("response");
        Map root = Utils.standardRequest(context, path, r);
        if (root == null) {
            return false;
        }

        Map<String, Object> data = Utils.castToMap(context, proto, root);
        if (data == null) {
            return false;
        }

        Set<String> knownAttributes = new HashSet<String>();
        return Utils.runTestList(
            context, proto, data, knownAttributes, true,
            Arrays.asList(
                new Domain(false),
                new StandardResponse()
            )
        );
    }
}
