package de.rwth.i2.attestor.its;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class T2Invoker {
    private File t2Executable;

    public T2Invoker(String filePath) {
        File exe = new File(filePath);
        if (!exe.exists()) {
            throw new IllegalArgumentException("T2 executable not found at " + filePath);
        }

        t2Executable = exe;
    }

    private Process invokeT2(Path workingDir, String... args) throws IOException {
        List<String> command = new ArrayList<>();
        command.add("mono");
        command.add(t2Executable.getAbsolutePath());
        command.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(command);

//        pb.redirectInput(ProcessBuilder.Redirect.INHERIT)
//                .redirectOutput(ProcessBuilder.Redirect.PIPE)
//                .redirectError(ProcessBuilder.Redirect.PIPE);

        pb.directory(workingDir.toFile());

        return pb.start();
    }

    public T2Result checkTermination(ITS its) throws IOException {
        Path dir = Files.createTempDirectory("attestor_its");
        Path file = Files.createTempFile(dir,"its", "t2");

        Files.write(file, its.toString().getBytes());

        Process t2 = invokeT2(dir, "--termination", file.toString());

        try {
            t2.waitFor(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            return T2Result.TIMEOUT;
        }

        if (t2.exitValue() != 0) {
            return T2Result.ERROR;
        }

        Scanner stream = new Scanner(t2.getInputStream());
        while (stream.hasNextLine()) {
            String line = stream.nextLine();
            if (line.startsWith("Termination proof succeeded")) {
                return T2Result.TERMINATING;
            }

            if (line.startsWith("Nontermination proof succeeded")) {
                return T2Result.NONTERMINATING;
            }

            if (line.startsWith("Termination/nontermination proof failed")) {
                return T2Result.MAYBE;
            }
        }

        Files.walk(dir)
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);

        return T2Result.ERROR;
    }
}
