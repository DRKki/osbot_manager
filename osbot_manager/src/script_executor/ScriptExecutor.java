package script_executor;

import bot_parameters.account.OSBotAccount;
import bot_parameters.configuration.Configuration;
import exceptions.ClientOutOfDateException;
import exceptions.IncorrectLoginException;
import exceptions.MissingWebWalkDataException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScriptExecutor {

    public static void execute(final String osbotPath, final OSBotAccount osBotAccount, final Configuration configuration) throws ClientOutOfDateException, MissingWebWalkDataException, IncorrectLoginException {

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(osbotPath);
        Collections.addAll(command, osBotAccount.toParameterString().split(" "));
        Collections.addAll(command, configuration.toParameterString().split(" "));

        System.out.println(command);

        try {

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            final Process process = processBuilder.start();
            System.out.println(processBuilder.command());
            try (final InputStream stdout = process.getInputStream();
                 final InputStreamReader inputStreamReader = new InputStreamReader(stdout);
                 final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String outputLine;
                while ((outputLine = bufferedReader.readLine()) != null) {

                    if (outputLine.toLowerCase().contains("out of date")) {
                        throw new ClientOutOfDateException();
                    } else if (outputLine.toLowerCase().contains("walking")) {
                        throw new MissingWebWalkDataException();
                    } else if (outputLine.toLowerCase().contains("invalid username or password")) {
                        throw new IncorrectLoginException();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
