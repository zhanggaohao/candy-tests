/*
 * Copyright [yyyy] [name of copyright owner]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * FileTest
 *
 * @author <a href="mailto:zhanggaohao@hotmail.com">张高豪</a>
 * @since 2023/10/31
 */
public class FileTest {

    @Test
    public void readFile() throws IOException {
        File source = new File("C:\\Users\\catch\\Desktop\\20031_DI84B21MA.txt");
        Charset charset = StandardCharsets.UTF_8;

        long start = System.nanoTime();
//        List<String> lines = Files.readAllLines(source.toPath(), charset);
//        List<String> lines = com.google.common.io.Files.readLines(source, charset);
//        List lines = FileUtils.readLines(source, charset.toString());
//        LineIterator lines = FileUtils.lineIterator(source, charset.toString());
//        int size = 0;
//        while (lines.hasNext()) {
//            lines.nextLine();
//            size++;
//        }
//        System.out.println(size);

        List<String> lines = readLines(new FileInputStream(source), charset);
        System.out.println(lines.size());
        System.out.println(System.nanoTime() - start);
    }

    private List<String> readLines(InputStream in, Charset charset) throws IOException {
        return readLines(new InputStreamReader(in, charset));
    }

    private List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List<String> lines = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            lines.add(line);
            line = reader.readLine();
        }
        return lines;
    }

    public static final String LINE_SEPARATOR;

    static {
        StringWriter buf = new StringWriter(4);
        PrintWriter out = new PrintWriter(buf);
        out.println();
        LINE_SEPARATOR = buf.toString();
    }

    private void writer(String content, Charset charset, File destFile) throws IOException {
        FileOutputStream out = new FileOutputStream(destFile);
        try (out) {
            out.write(content.getBytes(charset));
            out.write(LINE_SEPARATOR.getBytes(charset));
        }
    }

    private void writer(File destFile, Collection<String> lines, Charset charset) throws IOException {
        CharsetEncoder encoder = charset.newEncoder();
        try (OutputStream output = new FileOutputStream(destFile)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, encoder));
            for (String line : lines) {
                writer.append(line);
                writer.newLine();
            }
        }
    }

    private void writerLines(Collection<String> lines, Charset charset, File destFile) throws IOException {
        FileOutputStream out = new FileOutputStream(destFile);
        try (out) {
            for (String line : lines) {
                out.write(line.getBytes(charset));
                out.write(LINE_SEPARATOR.getBytes(charset));
            }
        }
    }

    private void writerFile(String msg) {
        try (FileWriter writer = new FileWriter("C:\\Users\\catch\\Desktop\\new.txt", true)) {
            writer.write(msg);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
