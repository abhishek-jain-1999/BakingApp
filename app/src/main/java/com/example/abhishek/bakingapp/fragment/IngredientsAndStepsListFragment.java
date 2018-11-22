package com.example.abhishek.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.bakingapp.adapter.StepsAdapter;
import com.example.abhishek.bakingapp.dataHolder.Ingredients;
import com.example.abhishek.bakingapp.dataHolder.RecipeDetail;
import com.example.abhishek.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IngredientsAndStepsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsAndStepsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.list_of_ingredients)
    TextView listOfIngredients;
    @BindView(R.id.measurement_of_ingredients)
    TextView measurementOfIngredients;
    @BindView(R.id.ingredients_card)
    CardView ingredientsCard;
    @BindView(R.id.steps_recycler_view)
    RecyclerView stepsRecyclerView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecipeDetail recipeDetail;

    private OnFragmentInteractionListener mListener;


    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientsAndStepsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientsAndStepsListFragment newInstance(String param1, String param2, RecipeDetail recipeDetail) {
        IngredientsAndStepsListFragment fragment = new IngredientsAndStepsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.recipeDetail = recipeDetail;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients_and_steps_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        setIngredientList();
        StepsAdapter stepsAdapter = new StepsAdapter(getContext(), recipeDetail.getSteps());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setAdapter(stepsAdapter);
        stepsRecyclerView.setLayoutManager(linearLayoutManager);

        stepsAdapter.onStepClickListener = new StepsAdapter.OnStepClickListener() {
            @Override
            public void OnStepClick(int i) {
                mListener.onFragmentInteraction(i);
            }
        };

        return view;
    }

    private void setIngredientList() {
        if (recipeDetail != null) {
            List<Ingredients> ingredientsList = recipeDetail.getIngredients();
            if (ingredientsList != null) {
                String s = "";
                for (Ingredients i : ingredientsList) {
                    s = s + i.getIngredient().toUpperCase() + " ( " + i.getQuantity() + " " + i.getMeasure() + " )\n";
                }
                s=s.substring(0,s.length()-1);
                listOfIngredients.setText(s);
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int i);
    }
}
