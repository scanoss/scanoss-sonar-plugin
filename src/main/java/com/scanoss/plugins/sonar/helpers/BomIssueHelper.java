package com.scanoss.plugins.sonar.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BomIssueHelper {

        private static class Bom {
            private final List<PurlInclude> include;

            public Bom(List<PurlInclude> include) {
                this.include = include;
            }
        }

        private static class PurlInclude {
            private final String purl;

            public PurlInclude(String purl) {
                this.purl = purl;
            }
        }

        private static class BomWrapper {
            private final Bom bom;

            public BomWrapper(Bom bom) {
                this.bom = bom;
            }
        }

        public static String createBomFixMessage(String[] purls) {
            List<PurlInclude> includes = Arrays.stream(purls)
                    .map(PurlInclude::new)
                    .collect(Collectors.toList());

            BomWrapper wrapper = new BomWrapper(new Bom(includes));

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            return gson.toJson(wrapper);
        }
}

