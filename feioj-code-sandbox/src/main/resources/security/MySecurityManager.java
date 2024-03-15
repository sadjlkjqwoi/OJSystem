
public class MySecurityManager extends SecurityManager {
    @Override
    public void checkExec(String cmd) {

        super.checkExec(cmd);
    }

    @Override
    public void checkRead(String file) {
//        super.checkRead(file);
    }

    @Override
    public void checkWrite(String file) {
//        super.checkWrite(file);
    }

    @Override
    public void checkDelete(String file) {
//        super.checkDelete(file);
    }

    @Override
    public void checkConnect(String host, int port) {
//        super.checkConnect(host, port);
    }
}
