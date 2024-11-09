import axios from 'axios';
import React, { useEffect, useState} from 'react'
import Navbar from '../components/Navbar';
import { SMALL_IMAGE_BASE_URL } from '../utils/constants';
import { useAuthStore } from '../store/authUser';
import { Link, Trash } from 'lucide-react';
import toast from 'react-hot-toast';

const SearchHistoryPage = () => {
    const [searchHistory, setSearchHistory] = useState([]);
    const {user} = useAuthStore();

    useEffect(() => {
        const getSearchHistory = async () => {
            try {
                const res = await axios.get(`/api/v1/search/history/${user.id}`);
                setSearchHistory(res.data.content);
            } catch(error) {
                console.error(error.message);
                setSearchHistory([]);
            }
        }
        getSearchHistory();
    }, [])
    console.log(searchHistory);
    if(searchHistory.length === 0){
        return (
            <div className='bg-black min-h-screen text-white'>
                <Navbar/>
                <div className='max-w-6xl mx-auto px-4 py-8'>
                    <h1 className='text-3xl font-bold mb-8'>Search History</h1>
                    <div className='flex justify-center items-center h-96'>
                        <p className='text-xl'>No search history found.</p>
                    </div>
                </div>
            </div>
        )
    }

    function formatDate(dateString) {
        const date = dateString.split(' ')
        return date[0] + " " + date[1] + " " + date[2]
    }
    
    const handleDelete = async (id) => {
        try {
            const res = await axios.delete(`/api/v1/search/history/${id}/${user.id}`)
            setSearchHistory(searchHistory.filter((item) => item.id != id))
            toast.success('Item deleted successfully!')
        } catch (error) {
            toast.error("Failed to delete search item!")
        }
    }
    
  return (
    <div className='bg-black min-h-screen text-white'>
       <Navbar/>
       <div className='max-w-6xl mx-auto px-4 py-8'>
            <h1 className='text-3xl font-bold mb-8'>Search History</h1>
            <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-3 gap-4'>
                {searchHistory?.map((entry) => 
                    entry.image && (
                        <div key={entry.id} className='bg-gray-900 rounded flex items-start p-4'>
                        <img src={SMALL_IMAGE_BASE_URL + entry.image} alt="History Image"  className='size-16 rounded-full object-cover mr-4'/>
                        <div className='flex flex-col'>
                            <span className='text-lg'>{entry.name || entry.title}</span>
                            <span className='text-xs text-gray-500'>{formatDate(entry.createdAt)}</span>
                        </div>

                        <h2 className={`text-center text-sm min-w-16 rounded-full p-1 ml-auto ${entry.searchType === "movie" ? "bg-red-500" : (entry.searchType === "tv" ? "bg-blue-500" : "bg-green-500")}`}>{entry.searchType[0].toUpperCase() + entry.searchType.slice(1)}</h2>
                        <Trash className='size-5 ml-4 cursor-pointer hover:fill-red-500 hover:text-red-600' onClick={() => handleDelete(entry.id)}/>
                    </div>
                    )
                    
                )

                }
            </div>
       </div>
    </div>
  )
}

export default SearchHistoryPage
