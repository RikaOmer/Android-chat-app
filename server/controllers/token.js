import isExists from "./userPass.js";
import express from "express";
import jwt from "jsonwebtoken";
const key = "B0ya s3cr4t K2y!!!!!";

const processLogin = async (username, password) => {
  // Check credentials
  const isUserExists = await isExists(username, password);
  if (isUserExists) {
    // GET USER FROM userPass !!!!!!!!!!!!!!!!!!!!!!!!
    const data = { username };
    // Generate the token.
    const token = jwt.sign(data, key);
    // Return the token to the browser
    return token;
  }
  // Incorrect username/password. The user should try again.
  else throw new Error("Incorrect username/password");
};

export default { processLogin };
