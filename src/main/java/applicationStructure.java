
/// ClientApplication

/*
1. Создать функционал для установления соединения с сервером.
2. Принимать входящий запрос с удаленного сервера с помощью протокола TCP/IP транспортного уровня,
    на изменение или устанавления нового значения.
 2.1 Функционал в отдельном потоке, транслирует запрос PDU-пакета в сеть modBus RTU с
    подключенными подчиненными устройствами, на обновления значения,
    с контрольной суммой возвращаемого ответа на доставку PDU-пакета.
3. Принимать входящий запрос с удаленного сервера по протоколу TCP/IP транспортного уровня,
    на запуск локального процесса, для записи, преобразования из потоковой передачи
    аудио и видеоданных в файл с расширением *.m4s, *.mpd, с динамически подключаемой
    библиотекой ffmpeg.dll, для последующей передачей ауди-видео файлов по протоколу UDP,
    на удаленный сервер.
    3.1 Интегрировать библиотеку ffmpeg.dll, для обработки мультимедийных данных.
    3.2 Создать фунционал по протоколу RTSP для управлением сеансом связи между сервером и клиентом,
      то есть между видеорегистратором и клиентским приложением для создания ауди-видео контента в виде файлов.
    3.3 Создать функцию удаления процесса по истечению t-времяни.
4. Реализовать фукционал создания, удаления и отправки файлов на удаленный сервер Nginx.
    4.1 Реализовать функцию создания ауди-видео файлов.
    4.2 Создать функцию передачи ауди-видео файлов на удаленный сервер.
    4.3 Создать функцию удаления ауди-видео файлов, которые были успешно оправлены на удаленный сервер.
*/


import java.io.FileWriter;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

class applicationStructure {

    // Models

    class Parameter {

        private static final long serialVersionUID = 1L;


        public UUID id;
        private java.sql.Date datetime;
        private String name;
        private String codParameter;
        private String lastUpdate;
        private int meaning;
        private int dumaId;// внешний ключь
        //public Duma duma  = new Duma();//навигационное свойство


        public Parameter(String name, String extension) {
            this.name = name;
        }

        public String getName(){ return name; }


        public int getMeaning(){ return meaning; }


        public int setMeaning(int meaning){ return this.meaning = meaning; }

    }


    class DataFile {

        public UUID id;
        public String nameFile;
        public java.util.Date dateTime;
        public Integer filesize;
        public byte[] data;
        public String extension;
        public Integer indexFile;
        public Integer numberFolder;
        public Integer headerSize;


        public DataFile(String nameFile) {
            this.nameFile = nameFile;
        }


        public String getName(){ return this.nameFile; }


        public String setExtension(String extension){
            return this.extension = extension;
        }
    }

    public class Duma {

        public UUID id;
        public String guid;
        public java.util.Date datetime;
        public String macAdress;
        public String ipAdress;
        public int port;
        public String status;
        public int number;
    }

    class AdressVideoChannel{}

    //-----------------------

    class Client{

        /*
            1. Создать функционал для установления соединения с сервером:
               * Создать функцию Connect() подключение клиента с удаленным сервером, для этого в объекте Socket
                  в аргументах конструктора прописываем ip - адрес и порт.
            2. Принимать входящий запрос с удаленного сервера с помощью протокола TCP/IP транспортного уровня,
                на изменение или устанавления нового значения.
               * Добавить JSSE функциональность для шифрования данных, аутентификации сервера,
                   целостности сообщения и дополнительной аутентификации клиента.
               * Создать обобщенный объект типа ReadWriteStreamAndReturnGenericObject, с одним из методов
                 modelDeserialization(), с возвращаемым обобщенным типом T.
               * Создать интерфейс SendDataParameter, для возврата в методе modelDeserialization
                  десереализованного объекта, т.е. добавить слабасвязанность и универсальность.
               * Создать функцию SendDataToServer(), для отправки на сервер объекта типа SendDataParameter,
                  с обновленными данными.
               * Создать функцию ReceiveModel(), для получения объекта типа SendDataParameter,
                  об успешном подключений нового клиента на сервере.
               * Создать функцию Disconnect(), для закрытия объекта Socket.
               * В методах реализовать блок конструкций try/catch на ошибки ввода-вывода т.е IOException,
                 при исключений вода-вывода в методе closeEverything() закрыть входящий поток и сокет,
                 вывести стек-трейс консоли при возникновений ошибки.
        */


        void Connect(){}


        void listenForModel(){}


        void SendDataToServer(){}


        void ReceiveModel(){}


        void commandSwitch(){}


        void Disconnect(){}


        void closeEverything(Socket socket){}
    }

    class RTUModbus{

        /*
            2.1 Функционал в отдельном потоке, транслирует запрос PDU-пакета в сеть modBus RTU с
                    подключенными подчиненными устройствами, на обновления значения,
                    с контрольной суммой возвращаемого ответа на доставку PDU-пакета:
                 * Создать функцию PortLoad(), для подключения к сети modBus RTU, для этого
                    необходимо открыть локальный порт, передать в аргументе конструктора
                    значения ip - адрес и порт локального сервера. Реализовать блок
                    конструкций try/catch при возникновений ошибки ввода-вывода, закрыть
                    соединение подключения.
                 * Создать функцию sendPackagePdu(String pdu), для передачи PDU-пакета
                    подчиненным устройствам. Получить обновленный ответ.
                    Реализовать блок конструкций try/catch при возникновений ошибки ввода-вывода,
                    закрыть соединение подключения.
                 *  Создать функцию CRC16(byte[] data), об успешности отправки PDU-пакета.

        */

        void PortLoad(){}

        List<String> sendPackagePdu(String pdu){return null;}

        void ParsingPdu(String pdu){}

        byte[] Frame(){return new byte[3]; }

        byte[] CRC16(byte[] data){return new byte[3];}

    }


    class StartNewProcess{

        /*
            3 Принимать входящий запрос с удаленного сервера по протоколу TCP/IP транспортного уровня,
                на запуск локального процесса, для записи, преобразования из потоковой передачи
                аудио и видеоданных в файл с расширением *.m4s, *.mpd, с динамически подключаемой
                библиотекой ffmpeg.dll, для последующей передачей ауди-видео файлов по протоколу UDP,
                на удаленный сервер:
                 * Создать функцию Proc_cmd(), для запуска нового процесса библиотеки ffmpeg.
                 * Задать задержку 1000 миллисекунд.
                 * Создать фукцию удаления запущенного процесса, для этого подътянуть запущенные
                     процессы из массива, найти процесс ffmpeg с последующим закрытием,
                     реализовать задержку рабочего потока на 250 миллисекунд.
        */


        void procCmd(AdressVideoChannel adressVideo){}

        void killProc(){}
    }


    class createSendDeleteVideoFilesOnClient {

        /*
            4. Реализовать фукционал создания, удаления и отправки файлов на удаленный сервер Nginx.
              * Создать метод createAnFiles(), для создания видео файлов,
                  который имеет параметры настройки потоковой передачи по протоколу RTSP и
                  включают следующее:
                   * Строка подключения к видеорегистратору, т.е. логин, пороль, канал(видео камера)
                   * Путь расположения создаваемых видео файлов, а также имя и расширение.
                   * Параметры кодирования и ссылка на поток RTSP , а именно:
                     * Имя библиотеки для запуска ffmpeg.
                     * По какому каналу идет стриминг rtsp_transport tcp/udp.
                     * Ссылку на строку подключения к видеорегистратору.
                     * Количество кадров в секунду = 25.
                     * Форматирования файла dash.mpd.
                     * Ссылку на строку подключения пути расположения видео файлов на выходе.
               * Создать метод отправки видео файлов, для этого необходимо следующее:
                 * Создать сереализуемый объект и следующие свойства:
                    * Заголовок о количестве байтов, передаваемого файла.
                    * Имя файла.
                    * ...
                 * Открыть отправляемый файл, считать поток.
                 * Сериализовать отправляемый объект.
                 * Создать массив типа byte, для сеарилизуемого объекта.
                 * Добавить сериализованный объект в массив.
                 * Создать массив типа byte, для добавления заголовка, о количестве отправляемых байтов и
                   сереализованного оправляемого файла.
                 * Записать в исходящий поток созданного массива, на удаленный сервер.
                 * Закрыть открытый файл.
               *  Создать функцию удаление файлов, после успешной записи на удаленный сервер.

        */


        void createAnFiles(AdressVideoChannel adressVideo, Process process){}


        void SendVideoDataOnVds(Object original_file, FileWriter stream_file, int index_file){}


        void deleteFiles(){}
    }
}