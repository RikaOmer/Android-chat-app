import mongoose from "mongoose";
import user from "./users.js";
const scheme = mongoose.Schema;

const message = new scheme({
  created: {
    type: Date,
    default: Date.now,
  },
  sender: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: false,
  },

  content: {
    type: String,
    required: true,
  },
});

export default mongoose.model("message", message);
