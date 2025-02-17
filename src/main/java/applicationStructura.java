
/// ClientApplication

/*
1 Создать функционал для установления соединения с сервером.
2 Принимать входящий запрос с удаленного сервера с помощью протокола TCP/IP транспортного уровня,
    на изменение или устонавления нового значения.
 2.1 Фунционал в отдельном потоке, транслирует запрос PDU-пакета в сеть modBus RTU с
    подключенным подчиненными устройствами, на обновления значения,
    с контрольной суммой возвращаемого ответа на доставку PDU-пакета.
3 Принимать входящий запрос с удаленного сервера по протоколу TCP/IP транспортного уровня,
    на запуск локального процесса, для записи, преобразования и потоковой передачи аудио и видеоданных с
    динамически подключаемой библиотекой ffmpeg.dll, для последующей передачей ауди-видео файлов
    по протоколу UDP, на удаленный сервер.
  3.1 Интегрировать библиотеку ffmpeg.dll, для обработки мультимедийных данных.
  3.2 Создать фунционал по протоколу RTSP для управлением сеансом связи между сервером и клиентом,
    то есть между видеорегистратором и клиентским приложением для создания ауди-видео контента
    в виде файлов.
  3.3 Создать функцию передачи ауди-видео файлов на удаленный сервер.
  3.4 Создать функцию удаления ауди-видео файлов которые были успешно оправлены на удаленный сервер.
  3.5 Создать функцию удаления процесса по истечению t-времяни.
*/


import java.io.FileWriter;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

class applicationStructura{

    // Models

    class Parameter{

        private static final long serialVersionUID = 1L;

        public int id;
        public Date datetime;
        public String name;
        public String codParameter;
        public String lastUpdate;
        public int meaning;
        public int dumaId;// внешний ключь
        //public Duma duma  = new Duma();//навигационное свойство

        public String getName(){ return name; }
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
        public String migration;
        public int age;
    }

    class AdressVideoChannel{}

    //-----------------------

    class Socket{

        void Start(){}

        void Connect(){}

        void ReceiveMessage(){}

        void ReadCallback(){}

        void Disconnect(){}
    }

    class RTUModbas{

        void PortLoad(){}

        List<String> SendMsg(String pdu){return null;}

        void ParsingPdu(String pdu){}

        byte[] Frame(){return new byte[3]; }

        byte[] CRC16(byte[] data){return new byte[3];}

    }

    class CreateWriteDeleteVideoFilesOnServer{

        void SendFiles(){}

        void Sending_file(Object[] fileInfo, AdressVideoChannel adressVideo){}

        void SendData(Object original_file, FileWriter stream_file, int index_file){
            // Состав отсылаемого универсального сообщения
            // 1. Заголовок о следующим объектом класса подробной информации дальнейших байтов
            // 2. Объект класса подробной информации о следующих байтах
            // 3. Байты непосредственно готовых к записи в файл или для чего-то иного.
        }
    }

    class StartNewProcess{

        void Proc_cmd(AdressVideoChannel adressVideo){}

        void Kill_proc(){}

        void CommandInput(AdressVideoChannel adressVideo, Process process){}
    }
}