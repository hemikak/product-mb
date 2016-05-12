/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.mb.tools.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Login CLI which validates the user.
 */
public class LoginCli {
    private static final Logger log = Logger.getLogger(LoginCli.class.getName());
    private String[] args = null;
    private Options options = new Options();

    /**
     * Initializing valid arguments.
     *
     * @param args String arguments passed in CLI.
     */
    public LoginCli(String[] args) {
        this.args = args;

        options.addOption("h", "help", false, "Show help.");
        options.addOption("u", "username", true, "Set username for login.");
        options.addOption("p", "password", true, "Set password for login.");
    }

    /**
     * Validates arguments and returns user key after authentication.
     *
     * @return The user's key
     */
    public String parseLogin() {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("u") && cmd.hasOption("p")) {
                String username = cmd.getOptionValue("u");
                String password = cmd.getOptionValue("p");
                if (!username.isEmpty() && !password.isEmpty()) {
                    //Validate username and password
                    if ("admin".equals(username) && "admin".equals(password)) {
                        // Return key for the user.
                        return "key";
                    } else {
                        log.log(Level.SEVERE, "Invalid username and password.");
                        return null;
                    }
                } else {
                    log.log(Level.SEVERE, "Username and password cannot be empty.");
                    return null;
                }
            } else {
                log.log(Level.SEVERE, "Missing either or both \"-u\" and \"-p\" options for login.");
                help();
            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
            help();
        }

        return null;
    }

    /**
     * Prints the help on CLI
     */
    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Main", options);
    }
}
