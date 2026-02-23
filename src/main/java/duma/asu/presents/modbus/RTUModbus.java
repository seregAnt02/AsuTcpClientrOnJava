package duma.asu.presents.modbus;

/*import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;*/
import duma.asu.models.interfaces.SendDataParameter;
import duma.asu.models.modbus.PduPackage;
import duma.asu.models.serializableModels.Parameter;
import duma.asu.presents.SSLSocketClient;
import jssc.*;

import java.util.HexFormat;


public class RTUModbus extends Thread{
    static SerialPort serialPort;
    private SSLSocketClient client;
    public RTUModbus(SSLSocketClient client) {
        this.client = client;
    }
    private PduPackage pduPackage = new PduPackage();

    @Override
    public void run() {
        //Передаём в конструктор имя порта
        serialPort = new SerialPort("/dev/ttyUSB0");
        try {
            //Открываем порт
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            //Отправляем запрос устройству
            parsingPdu("10 03 02 06 00 01"); //Hex 10 Dec 16
            // пакет modBus
            byte[] frame = frame(); //"16 03 02 06 00 01
            // send in modbus network
            serialPort.writeBytes(frame);
            frame = null;
            Thread.sleep(3000);
        }
        catch (SerialPortException | InterruptedException ex) {
            System.out.println(ex);
        }
    }



    private void dataModbus(SendDataParameter sendDataParameter){
        try {
            client.sendDataToServer(sendDataParameter);
            sendDataParameter = null;
        } catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    

    private void parsingPdu(String pdu) {

        pduPackage.hing_volume = null;
        pduPackage.low_volume = null;

        String[] arrayPduPacked = pdu.split(" ");
        pduPackage.slave_adress = Byte.parseByte(arrayPduPacked[0], 16); //16 dec
        pduPackage.function_code = Integer.parseUnsignedInt(arrayPduPacked[1]);
        pduPackage.start_adress_high = Integer.parseUnsignedInt(arrayPduPacked[2], 16);// hex
        pduPackage.start_adress_low = Integer.parseUnsignedInt(arrayPduPacked[3], 16);// hex
        pduPackage.high_count = Integer.parseUnsignedInt(arrayPduPacked[4], 16);
        pduPackage.low_count = Integer.parseUnsignedInt(arrayPduPacked[5], 16);
        pduPackage.hing_volume = pduPackage.hing_volume == null && arrayPduPacked.length > 6 ? arrayPduPacked[6] : "00";
        pduPackage.low_volume = pduPackage.low_volume == null && arrayPduPacked.length > 6 ? arrayPduPacked[7] : "00";

        arrayPduPacked = null;
        pdu = null;
    }

    private byte[] frame() {
        String[] hiVolume = pduPackage.hing_volume.split(";");
        String[] loVolume = pduPackage.low_volume.split(";");
        int y = 4; int countByte = 0;
        byte[] frame = new byte[8];
        //15 (0x0F) Запись нескольких DO
        //16 (0x10) Запись нескольких AO
        if (pduPackage.function_code != 15 && pduPackage.function_code != 16)
        {
            /*HexFormat hex = HexFormat.of();
            byte b = 127;
            String byteStr = hex.toHexDigits(b);
            byte byteVal = (byte)hex.fromHexDigits(byteStr);*/

            for (int x = 0; x < loVolume.length; x++)
            {
                frame[y++] = (byte) HexFormat.fromHexDigits(hiVolume[x]);//(byte)Integer.parseInt(hiVolume[x], 16);
                frame[y++] = (byte)HexFormat.fromHexDigits(loVolume[x]);
            }
        }
        if (pduPackage.function_code == 15)
        {
            countByte = pduPackage.high_count + pduPackage.low_count / 8; if (countByte > 0) countByte++;
            frame = new byte[8 + countByte]; y = 6;
            for (int x = 0; x < loVolume.length; x++)
            {
                frame[y++] = (byte)Integer.parseUnsignedInt(loVolume[x], 16);
            }
        }
        if (pduPackage.function_code == 16)
        {
            countByte = pduPackage.high_count + pduPackage.low_count * 2;
            frame = new byte[8 + countByte]; y = 6;
            for (int x = 0; x < loVolume.length; x++)
            {
                frame[y++] = (byte)Integer.parseUnsignedInt(hiVolume[x], 16);
                frame[y++] = (byte)Integer.parseUnsignedInt(loVolume[x], 16);
            }
        }
        frame[0] = pduPackage.slave_adress;
        frame[1] = (byte) pduPackage.function_code;
        frame[2] = (byte) pduPackage.start_adress_high;
        frame[3] = (byte) pduPackage.start_adress_low;
        frame[4] = (byte) pduPackage.high_count;
        frame[5] = (byte) pduPackage.low_count;
        byte[] checkSum = crc16(frame);
        frame[y++] = checkSum[0];
        frame[y] = checkSum[1];

        hiVolume = null;
        loVolume = null;
        checkSum = null;

        return frame;
    }

    private byte[] crc16(byte[] data)//конторольная сумма
    {
        byte[] checkSum = new byte[2];
        long rec_crc = 0XFFFF;
        for (int i = 0; i < data.length - 2; i++)
        {
            rec_crc ^= data[i];
            for (int j = 0; j < 8; j++)
            {
                if ((rec_crc & 0x01) == 1)
                {
                    rec_crc = (long) ((rec_crc >> 1) ^ 0xA001);
                }
                else
                {
                    rec_crc = (long) (rec_crc >> 1);
                }
            }
            checkSum[1] = (byte)((rec_crc >> 8) & 0xFF);
            checkSum[0] = (byte)(rec_crc & 0xFF);
        }

        data = null;

        return checkSum;
    }

    //состояние значения Low и Hi
    /*internal string SignalState(string meaning) {
        string data = null;
        if (meaning == "0") data = "00 00";
        if (meaning == "1") data = "FF 00";
        if (meaning == "FF" || meaning == "01") data = "1";// !! переделать
        if (meaning == "00") data = "0";
        return data;
    }*/


    private class PortReader implements SerialPortEventListener {
        private SendDataParameter parsHexToParameter(byte[] pduResponse){
            String pduHex = HexFormat.of().formatHex(pduResponse);
            Parameter parameter = new Parameter("asd", null);
            //parameter.setLastUpdate(pduHex);
            //parameter.setName(client.getClientName());
            System.out.print("\r\n--------\r\n");
            System.out.print(pduHex);
            System.out.print("\r\n--------\r\n");
            pduHex = null;
            pduResponse = null;
            return parameter;
        }

        
        public void serialEvent(SerialPortEvent event)
        {
            if(event.isRXCHAR() && event.getEventValue() > 0){
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    byte[] data = serialPort.readBytes();
                    Thread.sleep(300);
                    if(data != null){
                        // parsHexToInt
                        SendDataParameter sendDataParameter = parsHexToParameter(data);
                        // send data
                        dataModbus(sendDataParameter);
                    }
                    serialPort.closePort();
                    Thread.sleep(100);
                    serialPort = null;
                    data = null;
                }
                catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
