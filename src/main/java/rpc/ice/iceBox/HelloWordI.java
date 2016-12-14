package rpc.ice.iceBox;

import Ice.Communicator;
import Ice.Current;
import Ice.ObjectAdapter;
import IceBox.Service;
import rpc.ice.hello._MyServiceDisp;

public class HelloWordI extends _MyServiceDisp implements Service{

	private static final long serialVersionUID = 4807876044309497855L;
	
	private ObjectAdapter adapter;

	@Override
	public String hellowd(Current __current) {
		return "hello word";
	}

	@Override
	public void start(String name, Communicator communicator, String[] args) {
		adapter = communicator.createObjectAdapter(name);
		Ice.Object object = this;
		Ice.Identity identity = new Ice.Identity();
        identity.category = "IceBoxService";
        identity.name = name;
        adapter.add(object, identity);
        //direct proxy IceBoxService/HelloWord -t -e 1.1:tcp -h localhost -p 10809 -t 60000
        //indirect proxy IceBoxService/HelloWord -t -e 1.1
		adapter.activate();
	}

	@Override
	public void stop() {
		adapter.destroy();
	}

}
