import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class Display {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Integer> used = new ArrayList<Integer>();
		Random num = new Random();
		int index = num.nextInt(15)+1;
		int count = 1;
		int total = 15;
		
		Firebase main = new Firebase("https://clues309.firebaseIO.com");
		while(used.contains(index))
		{
			index = num.nextInt(15) + 1;
		}
		used.add(index);
		System.out.println(count + " of " + total);
		String url = "https://clues309.firebaseIO.com/clues/" + index + "/clue1";
		Firebase clues = new Firebase(url);
		clues.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		        System.out.println(snapshot.getValue());
		    }

			@Override
			public void onCancelled(FirebaseError arg0) {
				System.err.println("Listener was cancelled");
				   
				
			}
		});
		
	}

}
