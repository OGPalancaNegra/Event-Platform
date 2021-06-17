package Activities



import Models.EventOrganizers
import Register.eoRegistration
import ValueEventListenerAdapter
import com.google.firebase.database.FirebaseDatabase
import org.junit.Test

class eoLoginTest{

    @Test
    fun emptyReturnsFalse() {
        val result = eoRegistration().registerUser("", "", "")

        //eoLoginTest().

        val test = eoLoginTest()

        val correcteoLogin = FirebaseDatabase.getInstance().getReference("EventGoers").addListenerForSingleValueEvent(ValueEventListenerAdapter{
            val allEventOrganizersEmails= it.children.map { it.getValue(EventOrganizers::class.java)?.email }
            print(allEventOrganizersEmails)


           // test.login



        })


    }

}