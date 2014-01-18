package Resource;
import java.awt.Image;
import java.awt.Toolkit;

public final class ResourceLoader {
		static ResourceLoader rl = new ResourceLoader();
		public static Image getImage(String fileName){
			if (rl.getClass().getResource("/images/"+fileName) != null)
			return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource("/images/"+fileName));
			else
				return null;
			
		}
}
