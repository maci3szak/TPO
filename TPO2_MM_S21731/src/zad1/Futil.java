package zad1;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.StandardOpenOption.*;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {

        try {
            FileChannel dstChannel = FileChannel.open(Paths.get(resultFileName),CREATE,WRITE,TRUNCATE_EXISTING);
            Files.walkFileTree(Paths.get(dirName), new FileVisitor<Path>() {

                Charset outputCharset = Charset.forName("UTF-8");
                Charset inputCharset = Charset.forName("windows-1250");

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    FileChannel fileChannel = FileChannel.open(file);
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int)fileChannel.size());
                    fileChannel.read(byteBuffer);

                    while (fileChannel.read(byteBuffer) != -1) {
                        byteBuffer.flip();
                        CharBuffer charBuffer = inputCharset.decode(byteBuffer);
                        dstChannel.write(outputCharset.encode(charBuffer));

                        byteBuffer.clear();
                    }
                    fileChannel.close();

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
