import React, { useState } from 'react'
import { useContentStore } from '../store/content';
import Navbar from '../components/Navbar';
import { Search } from 'lucide-react';
import toast from 'react-hot-toast';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { ORIGINAL_IMG_BASE_URL } from '../utils/constants';
import { useAuthStore } from '../store/authUser';

const SearchPage = () => {
    const [activeTab, setActiveTab] = useState("movie");
    const [result, setResult] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const {setContentType} = useContentStore();
    const {user} = useAuthStore();

    const handleTabClick = (tab) => {
        setActiveTab(tab)
        tab === "movie" ? setContentType("movie") : setContentType("tv")
        setResult([])
    }

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            console.log("sending user request - ", `/api/v1/search/${activeTab}/${searchTerm}/${user.id}`)
            const res = await axios.get(`/api/v1/search/${activeTab}/${searchTerm}/${user.id}`)
            setResult(res.data.content);
        } catch(error){
            if(error.response.status === 404) {
                toast.error("Nothing found, make sure you searching under right category.")
            } else {
                toast.error("Error occured : " + error);   
            }
        }
    }

  return (
    <div className='bg-black min-h-screen text-white'>
        <Navbar/>
        <div className='container mx-auto px-4 py-8'>
            <div className='flex justify-center gap-3 mb-4'>
                <button className={`py-2 px-4 rounded ${activeTab === "movie" ? "bg-red-600" : "bg-gray-800"} hover:bg-red-700`} onClick={() => handleTabClick("movie")}>Movies</button>
                <button className={`py-2 px-4 rounded ${activeTab === "tv" ? "bg-red-600" : "bg-gray-800"} hover:bg-red-700`} onClick={() => handleTabClick("tv")}>TV Shows</button>
                <button className={`py-2 px-4 rounded ${activeTab === "person" ? "bg-red-600" : "bg-gray-800"} hover:bg-red-700`} onClick={() => handleTabClick("person")}>Persons</button>
            </div>
            <form action="" className='flex gap-2 items-stretch mb-8 max-w-2xl mx-auto' onSubmit={handleSearch}>
                <input  className='w-full p-2 rounded bg-gray-800 text-white' type="text" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} placeholder={`Search for a ${activeTab}`}/>
                <button className='bg-red-600 hover:bg-red-700 text-white p-2 rounded'>
                    <Search className='size-6'/>
                </button>
            </form>

            <div className='grid grid-cols-1 sm:grid-col-2 md:grid-cols-3 lg:grid-cols-4 gap-4'>
                {result.map((res) => {
                    if(!res.poster_path && !res.profile_path) return null;
                    return (
                        <div key={res.id} className='bg-gray-900 p-4 rounded'>
                            {activeTab === "person" ? (
                                <Link to={"/actor/" + res.name} className='flex flex-col items-center'>
                                    <img src={ORIGINAL_IMG_BASE_URL + res.profile_path} alt={res.name} className='max-h-96 rounded mx-auto'/>
                                    <h2 className='mt-2 text-xl font-bold'>{res.name}</h2>
                                </Link>

                            ) : (
                                <Link to={"/watch/" + res.id} onClick={() => {setContentType(activeTab)}}>
                                    <img src={ORIGINAL_IMG_BASE_URL + res.poster_path} alt={res.title || res.name} className='w-full h-auto rounded'/>
                                    <h2 className='mt-2 text-xl font-bold'>{res.title || res.name}</h2>
                                </Link>
                            )}
                        </div>
                    )
                })}
            </div>
        </div>
    </div>
  )
}

export default SearchPage
