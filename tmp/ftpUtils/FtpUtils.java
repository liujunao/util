import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2017/8/2.
 */
public class FtpUtils {

    private FTPClient ftpClient = null;
    private String server;
    private int port;
    private String userName;
    private String password;

    public FtpUtils(String server, int port, String userName, String password) {
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    //连接服务器
    public boolean connectFtp(){
        if (ftpClient != null && ftpClient.isConnected()){
            return true;
        }
        ftpClient = new FTPClient();
        //连接
        try {
            ftpClient.connect(this.server,this.port);
            ftpClient.login(this.userName,this.password);
            setFtpClient(ftpClient);
            //检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)){
                this.closeFtp();
                System.out.println("FTP server refuse to connect!");
                System.exit(1);
            }
            System.out.println(this.server + " : " + this.port + " : " + this.userName
            + " : " + this.password);
            //设置上传模式 ：binary
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            return true;
        } catch (IOException e) {
            this.closeFtp();
            e.printStackTrace();
            return false;
        }
    }

    //关闭 ftp 连接
    public void closeFtp(){
        if (ftpClient != null && ftpClient.isConnected()){
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传文件到 FTP 服务器
     * @param localDirectoryAndFileName 本地文件目录和文件名
     * @param ftpFileName 上传到服务器的文件名
     * @param ftpDirectory FTP目录，如果目录不存在会自动创建
     * @return
     */
    public boolean upload(String localDirectoryAndFileName,String ftpFileName,String ftpDirectory){
        if (!ftpClient.isConnected()){
            return false;
        }
        boolean flag = false;
        if (ftpClient != null){
            File file = new File(localDirectoryAndFileName);
            FileInputStream fileInputStream = null;

            try {
                fileInputStream = new FileInputStream(file);
                //创建目录
                this.mkDir(ftpDirectory);
                ftpClient.setBufferSize(100000);
                ftpClient.setControlEncoding("UTF-8");
                //设置文件类型
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //上传
                flag = ftpClient.storeFile(new String(ftpFileName.getBytes("UTF-8"),"UTF-8"),fileInputStream);
                System.out.println("文件上传成功！");
            } catch (Exception e) {
                this.closeFtp();
                e.printStackTrace();
                return false;
            }finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 循环创建目录，并且创建完成后，设置工作目录为当前创建的目录下
     * @param ftpPath 需要创建的目录
     */
    private boolean mkDir(String ftpPath) {
        if (!ftpClient.isConnected()){
            return false;
        }
        //将路径中的斜杠统一
        ftpPath = unifySlash(ftpPath);
        System.out.println("ftpPath: " + ftpPath);
        return createDirectory(ftpPath);
    }

    /**
     * 从 FTP 服务器上下载文件
     * @param ftpDirectoryAndFileName ftp 服务器文件路径，以 dir 形式开始
     * @param localDirectoryAndFileName 保存到本地的目录
     * @return
     */
    public boolean download(String ftpDirectoryAndFileName,String localDirectoryAndFileName){
        if (!ftpClient.isConnected()){
            return false;
        }
        ftpClient.enterLocalPassiveMode();

        //将路径中的斜杠统一
        ftpDirectoryAndFileName = unifySlash(ftpDirectoryAndFileName);

        String filePath = ftpDirectoryAndFileName.substring(0,ftpDirectoryAndFileName.lastIndexOf('/'));
        String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf('/') + 1);
        this.changeDir(filePath);
        try {
            ftpClient.retrieveFile(new String(fileName.getBytes(),"UTF-8"),new FileOutputStream(localDirectoryAndFileName));
            //file
            System.out.println(ftpClient.getReplyString());
            System.out.println("从ftp服务器上下载文件： " + ftpDirectoryAndFileName + ",保存到： " + localDirectoryAndFileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 层层切换工作目录
     * @param filePath
     * @return 切换结果
     */
    private boolean changeDir(String filePath) {
        if (!ftpClient.isConnected()){
            return false;
        }
        //将路径中的斜杠统一
        filePath = unifySlash(filePath);

        return createDirectory(filePath);
    }

    //将路径中的斜杠统一
    private String unifySlash(String string){
        char[] chars = string.toCharArray();
        StringBuffer stringBuffer = new StringBuffer(256);
        for (int i = 0;i < chars.length;i++){
            if (chars[i] == '\\'){
                stringBuffer.append('/');
            }else {
                stringBuffer.append(chars[i]);
            }
        }
        return stringBuffer.toString();
    }

    //创建目录
    private boolean createDirectory(String fileName){
        try {
            if (fileName.indexOf('/') == -1) {
                //只有一层目录
                ftpClient.makeDirectory(new String(fileName.getBytes(), "UTF-8"));
                ftpClient.changeWorkingDirectory(new String(fileName.getBytes(), "UTF-8"));
            } else {
                //多层目录循环创建
                String[] paths = fileName.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "UTF-8"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "UTF-8"));
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //返回 FTP 目录下的文件列表
    public String[] getFileNameList(String pathName){
        try {
            return ftpClient.listNames(pathName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //删除 FTP 上的文件
    public boolean deleteFile(String ftpDirectoryAndFile){
        if (!ftpClient.isConnected()){
            return false;
        }
        try {
            return ftpClient.deleteFile(ftpDirectoryAndFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除 FTP 目录
    public boolean deleteDirectory(String ftpDirectory){
        if (!ftpClient.isConnected()){
            return false;
        }

        try {
            return ftpClient.removeDirectory(ftpDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //切换到父目录

}
