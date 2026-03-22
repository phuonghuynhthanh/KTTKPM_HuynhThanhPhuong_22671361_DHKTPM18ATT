import java.util.ArrayList;
import java.util.List;

public class FileSystemApp {
    public static void main(String[] args) {

        FileSystemComponent file1 = new File("document.txt", 120);
        FileSystemComponent file2 = new File("image.png", 300);
        FileSystemComponent file3 = new File("video.mp4", 1500);

        Folder root = new Folder("Root");
        Folder subFolder = new Folder("SubFolder");

        root.add(file1);
        root.add(subFolder);

        subFolder.add(file2);
        subFolder.add(file3);

        root.displayInfo();
    }
}


interface FileSystemComponent {
    void displayInfo();
}

class File implements FileSystemComponent {
    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public void displayInfo() {
        System.out.println("File: " + name + " (" + size + "KB)");
    }
}

class Folder implements FileSystemComponent {
    private String name;
    private List<FileSystemComponent> children = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    public void displayInfo() {
        System.out.println("Folder: " + name);
        for (FileSystemComponent component : children) {
            component.displayInfo();
        }
    }
}