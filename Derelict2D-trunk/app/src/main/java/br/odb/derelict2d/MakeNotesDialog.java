package br.odb.derelict2d;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MakeNotesDialog extends DialogFragment implements OnClickListener, OnItemSelectedListener {


    private ArrayList<String> notes;
    private EditText edtNote;
    private Spinner spnNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_make_notes,
                container, true);

        Derelict2DApplication game = ((Derelict2DApplication) getActivity().getApplication());
        notes = game.notes;


        edtNote = view.findViewById(R.id.edtNote);
        edtNote.setText(notes.get(1));

        spnNotes = view.findViewById(R.id.spnNotes);
        spnNotes.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, notes));

        spnNotes.setOnItemSelectedListener(this);

        view.findViewById(R.id.btnSaveNote).setOnClickListener(this);
        view.findViewById(R.id.btnNewNote).setOnClickListener(this);
        view.findViewById(R.id.btnDeleteNote).setOnClickListener(this);
        getDialog().setTitle("Notes");

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSaveNote:
                notes.add(edtNote.getText().toString());
                break;
            case R.id.btnNewNote:
                edtNote.setText("");
                spnNotes.setSelection(0);
                break;
            case R.id.btnDeleteNote:

                if (spnNotes.getSelectedItemPosition() > 1) {
                    notes.remove(spnNotes.getSelectedItem());
                }
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, notes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNotes.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {

        if (spnNotes.getSelectedItemPosition() > 0) {
            edtNote.setText(notes.get(spnNotes.getSelectedItemPosition()));
        } else {
            edtNote.setText("");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
