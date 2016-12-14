package rpc.ice.iceBox;

import rpc.ice.hello.MyServicePrx;
import rpc.ice.hello.MyServicePrxHelper;

public class SimpleClient{

	public static void main(String[] args) {
		int status = 0;
		Ice.Communicator ic = null;
		try {
			//初始化通信器
			ic = Ice.Util.initialize(args);
			//传入远程服务单元的名称,网络协议，IP及端口,构造一个Proxy对象
			Ice.ObjectPrx base = ic.stringToProxy("IceBoxService/HelloWord -t -e 1.1:tcp -h localhost -p 10809 -t 60000");
			System.out.println("=======");
			System.out.println(base.toString());
			System.out.println("=======");
			
			//通过checkedCast向下转型，获取MyService接口的远程，并同时检测根据传入的名称
			//获取服务电源是否OnlineBook的代理接口
			MyServicePrx prxy = MyServicePrxHelper.checkedCast(base); 
			if(prxy == null){
				throw new Error("Invalid proxy");
			}
			//调用服务方法
			String rt = prxy.hellowd();
			//String rt2 = prxy.hellowd2();
			System.out.println(rt);
			//System.out.println(rt2);
		} catch (Exception e) {
			e.printStackTrace();
			status = 1;
		} finally {
			if (ic != null) {
				ic.destroy();
			}
		}
		System.exit(status);
	}

}
