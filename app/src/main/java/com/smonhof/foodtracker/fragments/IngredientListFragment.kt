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
import com.smonhof.foodtracker.data.*
import com.smonhof.foodtracker.views.GroupTileView
import com.smonhof.foodtracker.views.IngredientTileView
import com.smonhof.foodtracker.databinding.FragmentIngredientlistBinding
import com.smonhof.foodtracker.fragments.arguments.IngredientListFragmentArguments
import com.smonhof.foodtracker.views.SnackTileView

class IngredientListFragment : Fragment() {
    private var _binding: FragmentIngredientlistBinding? = null

    private val binding get() = _binding!!
    private var _group = Group(Resource("Invalid Group"), emptyArray(), emptyArray(), emptyArray())
    private var _onIngredientSelected : ((IngredientAmount) -> Unit)? = null
    private var _onSnackSelected : ((IngredientSnack) -> Unit)? = null

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
        //_binding!!.debugAllContent.setOnClickListener{Log.e(null,_group.listContent(0))}
    }

    private fun fetchArguments(){
        val args = arguments?.get("ContainerGroup")
        if (args is IngredientListFragmentArguments){
            _group = args._group
            _onIngredientSelected = args._onIngredientAmountSelected
            _onSnackSelected = args._onSnackSelected
        }
        else {
            Log.e(null,"Cannot fetch arguments for IngredientListFragment")
        }
    }
    private fun instantiateObjects(view: View){
        val ingredientList = binding.ingredientlist
        for(sub in _group.subGroups) {
            if(sub.isEmpty(_onIngredientSelected == null, _onSnackSelected == null)){
                continue
            }
            val groupView = GroupTileView(view.context, sub, ::onGroupClicked)
            ingredientList.addView(groupView)
        }
        val onIngredientSelected = _onIngredientSelected
        if(onIngredientSelected != null){
            for(ing in _group.ingredients) {
                val ingredientView = IngredientTileView(view.context, ing, onIngredientSelected)
                ingredientList.addView(ingredientView)
            }
        }
        val onSnackSelected = _onSnackSelected
        if(onSnackSelected != null){
            for(snk in _group.snacks){
                val snackView = SnackTileView(view.context, snk, onSnackSelected)
                ingredientList.addView((snackView))
            }
        }
    }

    private fun onGroupClicked(group: Group){
        val bundle = bundleOf("ContainerGroup" to IngredientListFragmentArguments(group,_onIngredientSelected,_onSnackSelected))
        findNavController().navigate(R.id.action_IngredientList_to_IngredientList, bundle)
    }
}