package org.epnoi.uia.learner.relations.patterns;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.learner.relations.patterns.lexical.BigramSoftPatternModel;

public class RelationalPatternsModelSerializer {
	// ------------------------------------------------------------------------------------
	public static void serialize(String filename, RelationalPatternsModel model) throws EpnoiResourceAccessException {
		
		// save the object to file
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(model);

			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// read the object from file
		// save the object to file

	}

	// ------------------------------------------------------------------------------------

	public static RelationalPatternsModel deserialize(String filename) throws EpnoiResourceAccessException {
		BigramSoftPatternModel model = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			model = (BigramSoftPatternModel) in.readObject();
			in.close();
		} catch (Exception ex) {
			throw new EpnoiResourceAccessException(ex.getMessage());
		}
		return model;
	}

	// ------------------------------------------------------------------------------------

	public static void main(String[] args) {

	}
}