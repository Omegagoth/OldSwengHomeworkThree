/*
 * Copyright 2015 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Encapsulates the data in a quiz question returned by the SwEng server.
 *
 */
public class QuizQuestion
{
    private long mId;
    private String mOwner;
    private String mBody;
    private List<String> mAnswers;
    private int mSolutionIndex;
    private List<String> mTags;

    /**
     * Creates a new QuizQuestion instance from the question elements provided
     * as arguments.
     * @param id the numeric ID of the question
     * @param owner the name of the owner of the question
     * @param body the question body
     * @param answers a list of two or more possible question answers
     * @param solutionIndex the index in the answer list of the correct answer
     * @param tags a list of zero or more tags associated to the question
     */
    public QuizQuestion(long id, String owner, String body, List<String> answers,
            int solutionIndex, List<String> tags)
    {
        if (owner == null)
        {
            throw new NullPointerException("Owner is null");
        }
        if (body == null)
        {
            throw new NullPointerException("Body is null");
        }
        if (answers.size() < 2)
        {
            throw new IllegalArgumentException("Answer list must be size two or more");
        }
        for (String answer: answers)
        {
            if (answer == null)
            {
                throw new NullPointerException("Answer is null");
            }
        }
        if (solutionIndex < 0 || solutionIndex >= answers.size())
        {
            throw new IllegalArgumentException("Invalid solutionIndex value");
        }
        for (String tag: tags)
        {
            if (tag == null)
            {
                throw new NullPointerException("Tag is null");
            }
        }
        
        mId = id;
        mOwner = owner;
        mBody = body;
        mAnswers = new ArrayList<String>(answers);
        mSolutionIndex = solutionIndex;
        mTags = new ArrayList<String>(tags);
    }
    
    /**
     * Returns the question ID.
     */
    public long getID()
    {
        return mId;
    }
    
    /**
     * Returns the question owner.
     */
    public String getOwner()
    {
        return mOwner;
    }
    
    /**
     * Returns the question body.
     */
    public String getBody()
    {
        return mBody;
    }
    
    /**
     * Returns a list of the question answers.
     */
    public List<String> getAnswers()
    {
        return new ArrayList<String>(mAnswers);
    }
    
    /**
     * Returns the index of the solution in the answer list.
     */
    public int getSolutionIndex()
    {
        return mSolutionIndex;
    }
    
    /**
     * Returns a (possibly empty) list of question tags.
     */
    public List<String> getTags()
    {
        return new ArrayList<String>(mTags);
    }
    
    /**
     * Creates a new QuizQuestion object by parsing a JSON object in the format
     * returned by the quiz server.
     * @param jsonObject a {@link JSONObject} encoding.
     * @return a new QuizQuestion object.
     * @throws JSONException in case of malformed JSON.
     */
    public static QuizQuestion parseFromJSON(JSONObject jsonObject) throws JSONException
    {

        // Check that Strings are correct.
        if (!(jsonObject.get("question") instanceof String) ||
                !(jsonObject.get("owner") instanceof String)) {
            throw new JSONException("Invalid question structure");
        }

        JSONArray jsonAnswers = jsonObject.getJSONArray("answers");
        List<String> answers = new ArrayList<String>();
        for (int i = 0; i < jsonAnswers.length(); ++i)
        {
            // Check that Strings are correct.
            if (!(jsonAnswers.get(i) instanceof String))
            {
                throw new JSONException("Invalid question structure");
            }
            answers.add(jsonAnswers.getString(i));
        }
        
        JSONArray jsonTags = jsonObject.getJSONArray("tags");
        List<String> tags = new ArrayList<String>();

        for (int i = 0; i < jsonTags.length(); ++i)
        {
            if (!(jsonTags.get(i) instanceof String)) {
                throw new JSONException("Invalid question structure");
            }
            tags.add(jsonTags.getString(i));
        }
        
        try
        {
            return new QuizQuestion(
                    jsonObject.getLong("id"),
                    jsonObject.getString("owner"),
                    jsonObject.getString("question"),
                    answers,
                    jsonObject.getInt("solutionIndex"),
                    tags);
        }
        catch (IllegalArgumentException e)
        {
            throw new JSONException("Invalid question structure");
        }
        catch (NullPointerException e)
        {
            throw new JSONException("Invalid question structure");
        }
    }
}
