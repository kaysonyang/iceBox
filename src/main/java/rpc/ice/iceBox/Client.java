package rpc.ice.iceBox;

import rpc.ice.hello.MyServicePrx;
import rpc.ice.hello.MyServicePrxHelper;

public class Client extends Ice.Application {

	class ShutdownHook extends Thread
    {
        @Override
        public void
        run()
        {
            try
            {
                communicator().destroy();
            }
            catch(Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void
    menu()
    {
        System.out.println(
            "usage:\n" +
            "t: send greeting as twoway\n" +
            "o: send greeting as oneway\n" +
            "O: send greeting as batch oneway\n" +
            "d: send greeting as datagram\n" +
            "D: send greeting as batch datagram\n" +
            "f: flush all batch requests\n" +
            "x: exit\n" +
            "?: help\n");
    }

    @Override
    public int
    run(String[] args)
    {
        if(args.length > 0)
        {
            System.err.println(appName() + ": too many arguments");
            return 1;
        }

        //
        // Since this is an interactive demo we want to clear the
        // Application installed interrupt callback and install our
        // own shutdown hook.
        //
        setInterruptHook(new ShutdownHook());
        
        Ice.Identity identity = new Ice.Identity();
        identity.category = "IceBoxService";
        identity.name = "SimplePrinter";
        
        String mm = communicator().identityToString(identity);//"IceBoxService/SimplePrinter"
        MyServicePrx twoway = MyServicePrxHelper.checkedCast(
            communicator().stringToProxy(mm).ice_twoway().ice_timeout(-1).ice_secure(false));
        if(twoway == null)
        {
            System.err.println("invalid object reference");
            return 1;
        }
        MyServicePrx oneway = MyServicePrxHelper.uncheckedCast(twoway.ice_oneway());
        MyServicePrx batchOneway = MyServicePrxHelper.uncheckedCast(twoway.ice_batchOneway());
        MyServicePrx datagram = MyServicePrxHelper.uncheckedCast(twoway.ice_datagram());
        MyServicePrx batchDatagram = MyServicePrxHelper.uncheckedCast(twoway.ice_batchDatagram());

        menu();

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        String line = null;
        do
        {
            try
            {
                System.out.print("==> ");
                System.out.flush();
                line = in.readLine();
                if(line == null)
                {
                    break;
                }
                if(line.equals("t"))
                {
                    twoway.hellowd();
                }
                else if(line.equals("o"))
                {
                    oneway.hellowd();
                }
                else if(line.equals("O"))
                {
                    batchOneway.hellowd();
                }
                else if(line.equals("d"))
                {
                    datagram.hellowd();
                }
                else if(line.equals("D"))
                {
                    batchDatagram.hellowd();
                }
                else if(line.equals("f"))
                {
                    batchOneway.ice_flushBatchRequests();
                    batchDatagram.ice_flushBatchRequests();
                }
                else if(line.equals("x"))
                {
                    // Nothing to do
                }
                else if(line.equals("?"))
                {
                    menu();
                }
                else
                {
                    System.out.println("unknown command `" + line + "'");
                    menu();
                }
            }
            catch(java.io.IOException ex)
            {
                ex.printStackTrace();
            }
            catch(Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
        }
        while(!line.equals("x"));

        return 0;
    }

    public static void
    main(String[] args)
    {
        Client app = new Client();
        int status = app.main("Client", args, "config.properties");
        System.exit(status);
    }
}
