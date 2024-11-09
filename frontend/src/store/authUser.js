// zustand is a global state management library
import toast from 'react-hot-toast';
import {create} from 'zustand';
import { persist } from 'zustand/middleware';
import axios from 'axios';

export const useAuthStore = create(persist((set) => ({
    user: null,
    isCheckingAuth: true,
    isSigningUp: false,
    isLogginOut: false,
    isLogginIn:false,
    signup: async (credentials) => {
        set({isSigningUp: true})
        try {
            const response = await axios.post("/api/v1/signup", credentials);
            console.log(response)
            set({user: response.data.user, isSigningUp: false})
             toast.success("Account created successfully!")
        } catch(error) {
            console.log(error)
            toast.error(error.response.data.error || "An error occured!")
            set({isSigningUp:false, user: null})
        }
    },
    login: async (credentials) => {
        set({isLogginIn: true})
        try{
            const response = await axios.post("/api/v1/login", credentials);
            console.log(response);
            set({user: response.data.user, isLogginIn : false});
            toast.success("Logged in successfully!")
        } catch (error){
            console.log(error);
            toast.error(error.response.data.error || "An error occurred while logging in")
            set({isLogging:false, user:null})
        }
    },
    logout: async () => {
        set({isLogginOut : true})
        try {
            await axios.post("/api/v1/logout");
            set({user: null, isLogginOut: false});
            toast.success("Logged out successfully!")
        } catch (error) {
            set({isLogginOut: false});
            toast.error(error.response.data.error || "Logout failed!")
        }
    },
    authCheck: async () => {
        set({isCheckingAuth: true})
        try {
            const response = await axios.get("/api/v1/authcheck");
            set({isCheckingAuth: false});
        } catch (error) {
            console.log(error);
            set({isCheckingAuth: false, user: null})
        }
    }
})))