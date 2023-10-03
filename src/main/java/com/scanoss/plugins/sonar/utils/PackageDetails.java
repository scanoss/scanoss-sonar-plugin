// SPDX-License-Identifier: MIT
/*
 * Copyright (c) 2023, SCANOSS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.scanoss.plugins.sonar.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lookup information about the current package
 */
public class PackageDetails {
    private static String version;  // Package version if we can

    /**
     * Try to determine the current package version
     *
     * @return decoded package version or an empty string
     */
    public static synchronized String getVersion() {
        if (version == null) {
            // try to load from maven properties first
            try {
                Properties p = new Properties();
                InputStream is = PackageDetails.class.getResourceAsStream("/META-INF/maven/com.scanoss.plugins.sonar/scanoss-sonar-plugin/pom.properties");
                if (is != null) {
                    p.load(is);
                    version = p.getProperty("version", "");
                }
            } catch (IOException | RuntimeException e) {
                // ignore
            }
            // fallback to using Java API
            if (version == null) {
                Package aPackage = PackageDetails.class.getPackage();
                if (aPackage != null) {
                    version = aPackage.getImplementationVersion();
                    if (version == null) {
                        version = aPackage.getSpecificationVersion();
                    }
                }
            }
            // we could not compute the version so use a blank
            if (version == null) {
                version = "";
            }
        }
        return version;
    }

}