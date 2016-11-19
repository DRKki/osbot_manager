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
import java.util.Optional;

public class ScriptExecutor {

    public static Optional<Process> execute(final String osbotPath, final OSBotAccount osBotAccount, final Configuration configuration) throws ClientOutOfDateException, MissingWebWalkDataException, IncorrectLoginException, InterruptedException {

        List<String> command = new ArrayList<>();

        Collections.addAll(command, "java", "-jar", osbotPath);
        Collections.addAll(command, osBotAccount.toParameterString().split(" "));
        Collections.addAll(command, configuration.toParameterString().split(" "));

        System.out.println(String.join(" ", command));

        try {

            final ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            final Process process = processBuilder.start();

            try (final InputStream stdout = process.getInputStream();
                 final InputStreamReader inputStreamReader = new InputStreamReader(stdout);
                 final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String outputLine;
                while ((outputLine = bufferedReader.readLine()) != null) {

                    System.out.println(outputLine);

                    if (outputLine.toLowerCase().contains("out of date")) {
                        throw new ClientOutOfDateException();
                    } else if (outputLine.toLowerCase().contains("walking")) {
                        throw new MissingWebWalkDataException();
                    } else if (outputLine.toLowerCase().contains("invalid username or password")) {
                        throw new IncorrectLoginException();
                    } else if (outputLine.contains("OSBot is now ready!")) {
                        break;
                    }
                }
            }

            return Optional.of(process);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
