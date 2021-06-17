package Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class AccountSettingsAdapter (fm: FragmentManager) : FragmentStatePagerAdapter(fm)  {

    private val fragList = ArrayList<Fragment>()
    private val hashFrag = HashMap<Fragment, Int>()
    private val hashNum =  HashMap<String, Int>()
    private val hashName = HashMap<Int, String>()

    //first hashmap if have fragment object can get fragment number (fragment is key fragment number is value)
    //second if has name can get fragment number
    //thrid if have int can get name


    override fun getItem(position: Int): Fragment {
        return fragList[position]
    }


    override fun getCount(): Int {
        return fragList.size
    }


    fun addFragment(fragment: Fragment, fragName: String){
        fragList.add(fragment)
        hashFrag[fragment] = fragList.size-1
        hashNum[fragName] = fragList.size-1
        hashName[fragList.size-1] = fragName



    }

    //to return fragment with the name param

    fun getFragNumber(fragName: String): Int? {
        if (hashNum.containsKey(fragName)){

            return hashNum[fragName]
        } else {
            return null
        }


    }

    fun getFragNumberfromobect(fragment: Fragment): Int? {
        if (hashFrag.containsKey(fragment)){

            return hashFrag[fragment]
        } else {
            return null
        }


    }

}