package org.epnoi.knowledgebase.wikidata.ddbb;

import org.epnoi.model.WikidataView;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.SerializedObject;
import org.epnoi.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by cbadenes on 15/02/16.
 */
@Component
public class WikidataViewStorer {

    private static final Logger LOG = LoggerFactory.getLogger(WikidataViewStorer.class);

    private static final String LABELS      = "epnoi-labels";

    private static final String REVERSES    = "epnoi-reverses";

    private static final String RELATIONS   = "epnoi-relations";

    private static final String SEPARATOR   = "/";

    @Autowired
    UDM udm;

    public WikidataView load(String uri){
        WikidataView view = new WikidataView();

        // Load label dictionary
        Optional<Resource> r1 = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(composeURI(uri,LABELS));
        LOG.info("Labels found in DDBB ? " + r1.isPresent());
        if (r1.isPresent()){
            view.setLabelsDictionary(loadLabels((Set<String>) r1.get()));
        }

        // Load reverse dictionary
        Optional<Resource> r2 = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(composeURI(uri,REVERSES));
        LOG.info("Reverse Labels found in DDBB ? " + r2.isPresent());
        if (r2.isPresent()){
            view.setLabelsReverseDictionary(loadLabels((Set<String>) r2.get()));
        }


        // Load relations
        Optional<Resource> r3 = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(composeURI(uri,RELATIONS));
        LOG.info("Relations found in DDBB ? " + r3.isPresent());
        if (r3.isPresent()){
            view.setRelations(loadRelations((Set<String>) r2.get()));
        }

        return view;
    }


    private Map<String,Set<String>> loadLabels(Set<String> labels){

        Map<String,Set<String>> labelsMap = new HashMap<>();

        for(String label: labels){

            Optional<Resource> res = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(label);

            if (res.isPresent()){
                labelsMap.put(label, (Set<String>) res.get());
            }
        }
        return labelsMap;
    }

    private Map<String,Map<String,Set<String>>> loadRelations(Set<String> labels){

        Map<String,Map<String,Set<String>>> relMap = new HashMap<>();

        for(String label: labels){

            Optional<Resource> res = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(label);

            if (res.isPresent()){
                relMap.put(label, (Map<String, Set<String>>) res.get());
            }
        }
        return relMap;
    }

    public void save(WikidataView view){

        LOG.info("trying to save wikidata view: " + view.getUri() + "..");

        // Save labels
        for (String label: view.getLabelsDictionary().keySet()){
            saveObject(composeURI(view.getUri(), LABELS, label),view.getLabelsDictionary().get(label));
        }

        // Save Reverse Labels
        for (String label: view.getLabelsReverseDictionary().keySet()){
            saveObject(composeURI(view.getUri(), REVERSES, label),view.getLabelsReverseDictionary().get(label));
        }

        // Save Relations
        for (String label: view.getRelations().keySet()){
            saveObject(composeURI(view.getUri(), RELATIONS, label),view.getRelations().get(label));
        }

    }

    private void saveObject(String uri,Object instace){
        SerializedObject serializedObject = new SerializedObject();
        serializedObject.setUri(uri);
        serializedObject.setInstance(instace);
        udm.save(Resource.Type.SERIALIZED_OBJECT).with(serializedObject);
    }


    private String composeURI(String... uris){
        StringBuilder composedUri = new StringBuilder();
        for (String uri : uris){
            composedUri.append(uri).append(SEPARATOR);
        }
        return composedUri.toString();
    }


    //TODO unify in an abstract method
    public Map<String,Set<String>> readRelation(String uri, String id){

        Map<String,Set<String>> relations = new HashMap<>();

        Optional<Resource> res = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(composeURI(uri, RELATIONS, id));

        if (res.isPresent()){
            SerializedObject serializedObject = (SerializedObject) res.get();
            relations = (Map<String, Set<String>>) serializedObject.getInstance();
        }
        return relations;
    }

    //TODO unify in an abstract method
    public Set<String> readLabel(String uri, String id){

        Set<String> label = new HashSet<>();

        Optional<Resource> res = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(composeURI(uri, LABELS, id));

        if (res.isPresent()){
            SerializedObject serializedObject = (SerializedObject) res.get();
            label = (Set<String>) serializedObject.getInstance();
        }
        return label;
    }

    //TODO unify in an abstract method
    public Set<String> readReverseLabel(String uri, String id){

        Set<String> label = new HashSet<>();

        Optional<Resource> res = udm.read(Resource.Type.SERIALIZED_OBJECT).byUri(composeURI(uri, REVERSES, id));

        if (res.isPresent()){
            SerializedObject serializedObject = (SerializedObject) res.get();
            label = (Set<String>) serializedObject.getInstance();
        }
        return label;
    }
}
