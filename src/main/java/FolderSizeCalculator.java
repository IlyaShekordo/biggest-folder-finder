import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSizeCalculator extends RecursiveTask<Long> {
    private File folder;

    public FolderSizeCalculator(File folder) {
        this.folder = folder;
    }

    @Override
    protected Long compute() {
        if (folder.isFile()) {
            return folder.length();
        }
        File[] files = folder.listFiles();
        long sum = 0;
        List<FolderSizeCalculator> subTasks = new LinkedList<>();

        for(File file : files) {
            FolderSizeCalculator task = new FolderSizeCalculator(file);
            task.fork(); //star
            subTasks.add(task);
        }

        for (FolderSizeCalculator task : subTasks) {
            sum += task.join();
        }
        return sum;
    }
}
