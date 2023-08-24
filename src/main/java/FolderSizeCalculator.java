import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSizeCalculator extends RecursiveTask<Long> {
    private Node node;

    public FolderSizeCalculator(Node node) {
        this.node = node;
    }

    @Override
    protected Long compute() {
        if (node.getFolder().isFile()) {
            return node.getFolder().length();
        }
        File[] files = node.getFolder().listFiles();
        long sum = 0;
        List<FolderSizeCalculator> subTasks = new LinkedList<>();

        for(File file : files) {
            Node child = new Node(file);
            FolderSizeCalculator task = new FolderSizeCalculator(child);
            task.fork(); //start
            subTasks.add(task);
            node.addChild(child);
        }

        for (FolderSizeCalculator task : subTasks) {
            sum += task.join();
        }
        node.setSize(sum);
        return sum;
    }
}
