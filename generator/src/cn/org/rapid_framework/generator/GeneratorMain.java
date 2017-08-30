package cn.org.rapid_framework.generator;

/**
 * 注意：这里的类名不能写成Generator，因为目前这个类路径都与rapid-generator-4.0.6.jar中的Generator完全一样，若是命名一样
 * 会出现没有找到一些方法的异常
 */

public class GeneratorMain {
 

	/**
	 * 请直接修改以下代码调用不同的方法以执行相关生成任务.
	 * 执行main方法之前，必须在数据库中建立表，并且在generator.xml中做相应的配置
	 */
	public static void main(String[] args) {
		try {
			/**
			 * 运行这里生成自动代码,1是用springmvc模板
			 * 1. 持久层是hibernate
			 */
			generatorData(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generatorData(int gType) throws Exception {
		GeneratorFacade g = new GeneratorFacade();
		GeneratorProperties.setProperty("moduleName","user");//有时会以模块来分层,moduleName是模板下的文件夹名称
		g.printAllTableNames(); // 打印数据库中的表名称
//		g.deleteOutRootDir(); // 删除生成器的输出目录

		if (gType == 1) {
			// 通过数据库表生成文件,生成为springmvc为模板的根目录
			//第一个参数是数据库表名，第二个参数是模板文件夹
			g.generateByTable("sys_user", "generator/template");
		}
		// 打开文件夹
		Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot"));
	}
}