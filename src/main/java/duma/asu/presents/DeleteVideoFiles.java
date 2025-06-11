package duma.asu.presents;

import java.io.File;

public class DeleteVideoFiles extends Thread{


    private File file;

    public DeleteVideoFiles() {
        this.file = new File(Client.pathFileName);
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
                int number_file_to_delete = new XmlParser(this.file).number_file_to_delete();
                file_delete(31);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void file_delete(int number_file_to_delete) throws InterruptedException {

        File[] array_files = file.listFiles();
        for (int i = 0; i < array_files.length; i++){
            String[] name_file = array_files[i].getName().split("-");
            if(name_file.length == 3){
                String[] file_extension = name_file[2].split("\\.");
                int number_file = Integer.parseInt(file_extension[0]);
                if(file_extension.length == 2 && number_file <= number_file_to_delete){
                    if(array_files[i].delete()) {
                        System.out.println("Файл с номером: " + array_files[i].getName() + " удален.");
                        Thread.sleep(100);
                    }
                }
                file_extension = null;
            }
            name_file = null;
        }
        array_files = null;
    }
}
