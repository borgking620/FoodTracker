package com.smonhof.foodtracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smonhof.foodtracker.R
import com.smonhof.foodtracker.views.GroupTileView
import com.smonhof.foodtracker.views.IngredientTileView
import com.smonhof.foodtracker.data.Group
import com.smonhof.foodtracker.data.Ingredient
import com.smonhof.foodtracker.databinding.FragmentIngredientlistBinding
import com.smonhof.foodtracker.fragments.arguments.IngredientListFragmentArguments

class IngredientListFragment : Fragment() {
    private var _binding: FragmentIngredientlistBinding? = null

    private val binding get() = _binding!!
    private var _group = Group("Invalid Group", emptyArray(), emptyArray())
    private var _onSelected : (Ingredient) -> Unit = { ingredient ->  Log.e(null,"Click on " + ingredient.name + " has no feedback!")}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientlistBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArguments()
        instantiateObjects(view)
    }

    private fun fetchArguments(){
        val args = arguments?.get("ContainerGroup")
        if (args is IngredientListFragmentArguments){
            _group = args.group
            _onSelected = args.onIngredientSelected
        }
        else {
            Log.e(null,"Cannot fetch arguments for IngredientListFragment")
        }
    }
    private fun instantiateObjects(view: View){
        binding.headerTitle.setText(_group.name)
        val ingredientList = binding.ingredientlist
        for(sub in _group.subGroups) {
            val groupView = GroupTileView(view.context, sub, ::onGroupClicked)
            ingredientList.addView(groupView)
        }
        for(ing in _group.ingredients) {
            val ingredientView = IngredientTileView(view.context, ing, _onSelected)
            ingredientList.addView(ingredientView)
        }
    }

    private fun onGroupClicked(group: Group){
        val bundle = bundleOf("ContainerGroup" to IngredientListFragmentArguments(group,_onSelected))
        findNavController().navigate(R.id.action_IngredientList_to_IngredientList, bundle)
    }
}